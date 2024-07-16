package hust.server.domain.products.service;

import com.cloudinary.Cloudinary;
import com.spire.barcode.BarCodeGenerator;
import com.spire.barcode.BarCodeType;
import com.spire.barcode.BarcodeSettings;
import com.spire.barcode.QRCodeECL;
import hust.server.app.exception.ApiException;
import hust.server.domain.authen.entities.User;
import hust.server.domain.authen.repository.UserRepository;
import hust.server.domain.products.dto.request.AdminBranchCreationRequest;
import hust.server.domain.products.dto.request.AdminBranchUpdateRequest;
import hust.server.domain.products.dto.request.AdminMenuItemRequest;
import hust.server.domain.products.dto.request.TableRequest;
import hust.server.domain.products.dto.response.AdminBranchDetailsResponse;
import hust.server.domain.products.dto.response.AdminBranchFilterResponse;
import hust.server.domain.products.dto.response.AdminBranchResponse;
import hust.server.domain.products.dto.response.AdminMenuItemResponse;
import hust.server.domain.products.entity.*;
import hust.server.domain.products.repository.*;
import hust.server.infrastructure.enums.MessageCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.transaction.Transactional;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BranchService {
    private @Value("${ui.url}") String uiUrl;
    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Autowired
    private TableRepository tableRepository;

    public Map<String, Object> upload(File file)  {
        try{
            return cloudinary.uploader().upload(file, Map.of());
        }catch (IOException io){
            throw new RuntimeException("Image upload fail");
        }
    }

    public String generateQrCode(String code, String branchName, Long tableId) {
        if (code == null)throw new ApiException(MessageCode.BRANCH_CODE_IS_NULL);

        BarcodeSettings settings = new BarcodeSettings();
        //Set barcode type
        settings.setType(BarCodeType.QR_Code);
        //Set barcode data
        StringBuilder data = new StringBuilder(uiUrl);
        data.append("/authenticate?code=");
        data.append(code);
        if (tableId != null){
            data.append("&tableId=");
            data.append(tableId);
        }

        settings.setData(data.toString());
        //Set barcode module width
        settings.setX(2);
        //Set error correction level
        settings.setQRCodeECL(QRCodeECL.M);

        //Set top text
        settings.setTopText(branchName);
        //Set bottom text

        //Set text visibility
        settings.setShowText(false);
        settings.setShowTopText(true);
        settings.setShowBottomText(true);

        //Set border visibility
        settings.hasBorder(false);

        //Instantiate a BarCodeGenerator object based on the specific settings
        BarCodeGenerator barCodeGenerator = new BarCodeGenerator(settings);
        //Generate QR code image
        BufferedImage bufferedImage = barCodeGenerator.generateImage();
        File file = new File("QR_Code.png");
        //save the image to a .png file
        try {
            ImageIO.write(bufferedImage,"png", file);
        }catch (Exception e){
            throw new ApiException(e, MessageCode.FAIL);
        }

        Map<String, Object> res = upload(file);

        return res.get("url").toString();
    }

    public List<AdminBranchFilterResponse> getBranchesFilter(String userId) {
        List<Branch> branchList = branchRepository.getByCreatedBy(userId);
        return branchList.stream().map(Branch::toAdminBranchFilterResponse).collect(Collectors.toList());
    }

    public List<AdminBranchResponse> getBranches(String userId) {
        List<Branch> branchList = branchRepository.getByCreatedByAndSort(userId);

        return branchList.stream().map(item -> {
            AdminBranchResponse rspItem = item.toAdminBranchResponse();
            List<User> userList = userRepository.getManagerUser(item.getId());
            if (userList.isEmpty()) {
                rspItem.setManager("");
                rspItem.setPhoneManager("");
            }else {
                rspItem.setManager(userList.get(0).getName());
                rspItem.setPhoneManager(userList.get(0).getPhone());
            }
            return rspItem;
        }).collect(Collectors.toList());
    }

    public MessageCode createBranch(AdminBranchCreationRequest request) throws IOException {
        Branch branch = request.toBranchEntity();
        String url = generateQrCode(branch.getCode(), branch.getName(), null);
        branch.setQrcode(url);
        try {
            branchRepository.save(branch);
        }catch (Exception e){
            throw new ApiException(e, MessageCode.FAIL);
        }
        Menu menu = new Menu();
        menu.setCreatedBy(request.getCreatedBy());
        menu.setBranchId(branch.getId());
        menu.setActive(1);
        menu.setMenuItemList(new ArrayList<>());

        List<Product> products = productRepository.getByCreatedBy(request.getCreatedBy());
        for(Product item : products){
            MenuItem menuItem = new MenuItem();
            menuItem.setProductId(item.getId());
            menuItem.setActive(0);
            try{
                menuItemRepository.save(menuItem);
            }catch (Exception e){
                throw new ApiException(e, MessageCode.FAIL);
            }
            menu.getMenuItemList().add(menuItem);
        }
        try {
            menuRepository.save(menu);
        }catch (Exception e){
            throw new ApiException(e, MessageCode.FAIL);
        }
        return MessageCode.SUCCESS;
    }

    public MessageCode deleteBranch(Long id, String userId) {
        Branch branch = branchRepository.getByIdAndCreatedBy(id, userId).orElse(null);
        if (branch == null)throw new ApiException(MessageCode.ID_NOT_FOUND, "id = " + id + "; userId = " + userId);

        branch.setActive(0);
        try {
            branchRepository.save(branch);
            return MessageCode.SUCCESS;
        }catch (Exception e){
            throw new ApiException(e, MessageCode.FAIL);
        }
    }

    public AdminBranchDetailsResponse getBranchesDetails(Long id, String userId) {
        Branch branch = branchRepository.getByIdAndCreatedBy(id, userId).orElse(null);
        if (branch == null)throw new ApiException(MessageCode.ID_NOT_FOUND, "id = " + id + "; userId = " + userId);

        AdminBranchDetailsResponse response = branch.toAdminBranchDetailResponse();
        Menu menu = menuRepository.getByBranchIdAndActive(branch.getId(), 1).orElse(null);
        if (menu == null)throw new ApiException(MessageCode.ID_NOT_FOUND, "branchId = " + branch.getId());

        for (MenuItem item : menu.getMenuItemList()){
            Product product = productRepository.getByIdAndActive(item.getProductId(), 1).orElse(null);
            if (product == null)continue;
            AdminMenuItemResponse menuItemResponse = item.toAdminMenuItemResponse();
            menuItemResponse.setProductName(product.getName());
            menuItemResponse.setImg(product.getImg());
            response.getMenuItemRes().add(menuItemResponse);
        }

        return response;
    }

    @Transactional
    public MessageCode updateBranch(AdminBranchUpdateRequest request) {
        Branch branch = branchRepository.getByIdAndCreatedBy(request.getId(), request.getCreatedBy()).orElse(null);
        if (branch == null)throw new ApiException(MessageCode.ID_NOT_FOUND, "id = " + request.getId() + "; userId = " + request.getCreatedBy());

        branch.setName(request.getName());
        branch.setAddress(request.getAddress());
        branch.setCity(request.getCity());
        branch.setTown(request.getTown());
        branch.setLogo(request.getLogo());
        branch.setActive(request.getStatus());

        // update table

        List<Table> tableList = branch.getTableList();
        for (Table table: tableList){
            boolean isExist = request.getTableList().stream().anyMatch(item -> table.getId().equals(item.getId()));
            if (!isExist){
                table.setIsDeleted(1);
                tableRepository.save(table);
            }
        }

        for (TableRequest tableReq : request.getTableList()){
            if (tableReq.getId() == null || tableReq.getId() == 0){
                // create QR
                Table table = new Table();
                table.setName(tableReq.getName());
                table.setIsDeleted(0);
                tableRepository.save(table);
                String qrCode = generateQrCode(branch.getCode(), branch.getName(), table.getId());
                table.setQrcode(qrCode);
                tableRepository.save(table);
                branch.getTableList().add(table);
            }else {
                Table table = tableRepository.getById(tableReq.getId()).orElse(null);
                if (table == null)throw new ApiException(MessageCode.ID_NOT_FOUND, "tableId = " + tableReq.getId());
                if (!table.getName().equals(tableReq.getName()))table.setName(tableReq.getName());
                try {
                    tableRepository.save(table);
                }catch (Exception e){
                    throw new ApiException(e, MessageCode.FAIL);
                }
            }
        }


        try {
            branchRepository.save(branch);
        }catch (Exception e){
            throw new ApiException(e, MessageCode.FAIL);
        }

        // update menu
        for (AdminMenuItemRequest menuItemReq : request.getMenuItemRes()){
            Integer active = menuItemReq.getActive() ? 1 : 0;
            try {
                menuItemRepository.updateActive(menuItemReq.getId(), active);
            }catch (Exception e){
                throw new ApiException(e, MessageCode.FAIL);
            }
        }

        return MessageCode.SUCCESS;
    }

    public MessageCode changeQrCode(Long id) {
        Branch branch = branchRepository.getById(id).orElse(null);
        if (branch == null)throw new ApiException(MessageCode.ID_NOT_FOUND, "branchId = " + id);
        String qrCode = generateQrCode(branch.getCode(), branch.getName(), null);
        branch.setQrcode(qrCode);
        try {
            branchRepository.save(branch);
            return MessageCode.SUCCESS;
        }catch (Exception e){
            throw new ApiException(e, MessageCode.FAIL);
        }
    }

    public String getBranchName(String employeeId) {
        Branch branch = branchRepository.getByEmployeeId(employeeId);
        if (branch == null)throw new ApiException(MessageCode.FAIL);
        return branch.getName();
    }
}
