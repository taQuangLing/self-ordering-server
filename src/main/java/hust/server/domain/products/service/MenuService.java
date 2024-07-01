package hust.server.domain.products.service;

import hust.server.app.exception.ApiException;
import hust.server.domain.authen.entities.User;
import hust.server.domain.authen.repository.UserRepository;
import hust.server.domain.products.dto.response.*;
import hust.server.domain.products.entity.Branch;
import hust.server.domain.products.entity.Category;
import hust.server.domain.products.entity.Menu;
import hust.server.domain.products.entity.Product;
import hust.server.domain.products.repository.BranchRepository;
import hust.server.domain.products.repository.CategoryRepository;
import hust.server.domain.products.repository.MenuRepository;
import hust.server.domain.products.repository.ProductRepository;
import hust.server.infrastructure.enums.MessageCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MenuService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private UserRepository userRepository;

    public GuestMenuResponse getMenuForGuest(Long branchId) {
        // Check branch id
        Branch branch = branchRepository.getById(branchId).orElse(null);
        if (branch == null)throw new ApiException(MessageCode.BRANCH_NOT_EXIST);

        if (branch.getActive() == 0)throw new ApiException(MessageCode.BRANCH_INACTIVE);

        Menu menu = menuRepository.getByBranchIdAndActive(branchId, 1).orElse(null);
        if (menu == null)throw new ApiException(MessageCode.MENU_NOT_EXIST);

        List<Product> products = productRepository.getProductMenu(branchId);

        List<Category> categories = categoryRepository.getByActive(1);

        GuestMenuResponse response = new GuestMenuResponse();

        for(Category category : categories){
            if (category.getActive() == 0)continue;
            GuestCategoryResponse guestCategoryResponse = category.toCategoryGuestResponse();
            int count = 0;
            for (Product product : products){
                if (product.getCategoryId() == category.getId()){
                    count++;
                    GuestProductResponse guestProductResponse = product.toProductGuestResponse();
                    guestCategoryResponse.getProducts().add(guestProductResponse);
                }
            }
            if (count > 0){
                guestCategoryResponse.setCount(count);
                response.getCategories().add(guestCategoryResponse);
            }

        }

        return response;
    }

    public List<CashierCategoryResponse> cashierGetMenu(String userId) {
        User user = userRepository.getById(userId).orElse(null);
        if (user == null)throw new ApiException(MessageCode.ID_NOT_FOUND, "userId = " + userId);
        // Check branch id
        Branch branch = user.getBranch();
        if (branch == null)throw new ApiException(MessageCode.BRANCH_NOT_EXIST);

        if (branch.getActive() == 0)throw new ApiException(MessageCode.BRANCH_INACTIVE, "BranchId: " + branch.getId());

        Menu menu = menuRepository.getByBranchIdAndActive(branch.getId(), 1).orElse(null);
        if (menu == null)throw new ApiException(MessageCode.MENU_NOT_EXIST);

        List<Product> products = productRepository.getProductMenu(branch.getId());

        List<Category> categories = categoryRepository.getByActive(1);

        List<CashierCategoryResponse> response = new ArrayList<>();

        for(Category category : categories){
            if (category.getActive() == 0)continue;
            CashierCategoryResponse cashierCategoryResponse = category.toCashierCategoryResponse();
            int count = 0;
            for (Product product : products){
                if (product.getCategoryId() == category.getId()){
                    count++;
                    CashierProductResponse cashierProductResponse = product.toCashierProductResponse();
                    cashierCategoryResponse.getProducts().add(cashierProductResponse);
                }
            }
            if (count > 0){
                cashierCategoryResponse.setCount(count);
                response.add(cashierCategoryResponse);
            }
        }
        return response;
    }
}
