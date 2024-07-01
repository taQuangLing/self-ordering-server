package hust.server.domain.products.service;

import hust.server.app.exception.ApiException;
import hust.server.domain.products.dto.request.AdminCategoryRequest;
import hust.server.domain.products.dto.request.AdminProductRequest;
import hust.server.domain.products.dto.request.AdminProductUpdatedRequest;
import hust.server.domain.products.dto.request.AdminSizeRequest;
import hust.server.domain.products.dto.response.*;
import hust.server.domain.products.entity.*;
import hust.server.domain.products.repository.*;
import hust.server.infrastructure.enums.MessageCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Autowired
    private ProductSizeRepository productSizeRepository;

    public GuestProductDetailsResponse getProductDetails(Long id) {
        Product product = productRepository.getById(id).orElse(null);
        if (product == null)throw new ApiException(MessageCode.ID_NOT_FOUND);
        return product.toProductDetailsGuestResponse();
    }

    public List<AdminProductResponse> getProductsByAdmin(String userId) {
        List<Product> productList = productRepository.getByCreatedByAndSort(userId);

        return productList.stream().map(item -> {
            Category category = categoryRepository.getById(item.getCategoryId()).orElse(null);
            if (category == null)throw new ApiException(MessageCode.ID_NOT_FOUND, "categoryId = "+ item.getCategoryId());
            AdminProductResponse rspItem = item.toAdminProductResponse();
            rspItem.setCategoryName(category.getName());
            return rspItem;
        }).collect(Collectors.toList());
    }

    public List<AdminCategoryResponse> getCategories(String userId) {
        List<Category> categories = categoryRepository.getByCreatedBy(userId);
        return categories.stream().map(Category::toAdminCategoryResponse).collect(Collectors.toList());
    }

    @Transactional
    public MessageCode addProduct(AdminProductRequest request) {
        Product product = request.toProductEntity();
        try{
            productRepository.save(product);
            List<Menu> menuList = menuRepository.getByCreatedBy(request.getUserId());
            for (Menu item : menuList){
                MenuItem menuItem = new MenuItem();
                menuItem.setProductId(product.getId());
                if (request.getIsAllForMenu())menuItem.setActive(1);
                else menuItem.setActive(0);
                item.getMenuItemList().add(menuItem);
                try {
                    menuItemRepository.save(menuItem);
                    menuRepository.save(item);
                }catch (Exception e){
                    throw new ApiException(e, MessageCode.FAIL);
                }
            }


            return MessageCode.SUCCESS;
        }catch (Exception e) {
            throw new ApiException(e, MessageCode.FAIL);
        }
    }

    public MessageCode createCategory(AdminCategoryRequest request) {
        Category category = request.toCategoryEntity();
        try {
            categoryRepository.save(category);
            return MessageCode.SUCCESS;
        }catch (Exception e){
            throw new ApiException(e, MessageCode.FAIL);
        }
    }

    public MessageCode deleteProduct(Long id, String userId) {
        Product product = productRepository.getById(id).orElse(null);
        if (product == null)throw new ApiException(MessageCode.ID_NOT_FOUND, "productId = " + id);

        if (!product.getCreatedBy().equals(userId))throw new ApiException(MessageCode.RESOURCES_AUTHORIZATION);

        product.setActive(0);
        try {
            productRepository.save(product);
            return MessageCode.SUCCESS;
        }catch (Exception e){
            throw new ApiException(e, MessageCode.FAIL);
        }
    }

    public AdminProductDetailsResponse adminGetProductDetails(Long id, String userId) {
        Product product = productRepository.getByIdAndCreatedBy(id, userId).orElse(null);
        if (product == null)throw new ApiException(MessageCode.ID_NOT_FOUND, "id = " + id + ";userId = " + userId);

        return product.toAdminProductDetailsResponse();
    }

    @Transactional
    public MessageCode updateProduct(AdminProductUpdatedRequest request) {
        Product product = productRepository.getByIdAndCreatedBy(request.getId(), request.getUserId()).orElse(null);
        if (product == null)throw new ApiException(MessageCode.ID_NOT_FOUND, "id = " + request.getId() + ";userId = " + request.getUserId());

        if (request.getHasSize() == 1) {
            for (AdminSizeRequest sizeReq : request.getSizeRequestList()){
                ProductSize size = productSizeRepository.getById(sizeReq.getId()).orElse(null);
                if (size == null){
                    size = new ProductSize();
                    product.getSizeList().add(size);
                }
                size.setSize(sizeReq.getSize());
                size.setPrice(sizeReq.getPrice());
                size.setIsDefault(sizeReq.getIsDefault());
                try {
                    productSizeRepository.save(size);
                    sizeReq.setId(size.getId());
                }catch (Exception e){
                    throw new ApiException(e, MessageCode.FAIL);
                }
            }
            if (product.getSizeList() != null){
                for (ProductSize size : product.getSizeList()){
                    boolean isDup = false;
                    for(AdminSizeRequest sizeReq : request.getSizeRequestList()){
                        if (Objects.equals(size.getId(), sizeReq.getId())){
                            isDup = true;
                            break;
                        }
                    }
                    if (!isDup){
                        try {
                            productSizeRepository.delete(size);
                        }catch (Exception e){
                            throw new ApiException(e, MessageCode.FAIL);
                        }
                    }
                }
            }

        }
        product.setActive(request.getStatus());
        product.setPrice(request.getPrice());
        product.setDescription(request.getDescription());
        product.setName(request.getName());
        product.setImg(request.getImg());
        product.setHasSize(request.getHasSize());
        product.setCategoryId(request.getCategoryId());

        try {
            productRepository.save(product);
            return MessageCode.SUCCESS;
        }catch (Exception e){
            throw new ApiException(e, MessageCode.FAIL);
        }
    }
}
