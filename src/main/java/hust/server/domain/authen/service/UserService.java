package hust.server.domain.authen.service;

import hust.server.app.exception.ApiException;
import hust.server.domain.authen.dto.request.EmployeeUpdateRequest;
import hust.server.domain.authen.dto.request.TokenRequest;
import hust.server.domain.authen.dto.request.UserRegisterRequest;
import hust.server.domain.authen.dto.response.AuthResponse;
import hust.server.domain.authen.dto.response.EmployeeDetailsResponse;
import hust.server.domain.authen.dto.response.EmployeeResponse;
import hust.server.domain.authen.entities.CustomUserDetails;
import hust.server.domain.authen.entities.Position;
import hust.server.domain.authen.entities.User;
import hust.server.domain.authen.repository.PositionRepository;
import hust.server.domain.authen.repository.UserRepository;
import hust.server.domain.products.entity.Branch;
import hust.server.domain.products.repository.BranchRepository;
import hust.server.infrastructure.enums.MessageCode;
import hust.server.infrastructure.utilies.JwtUtil;
import hust.server.infrastructure.utilies.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    @Lazy
    private JwtUtil jwtTokenUntil;

    @Autowired
    private PositionRepository positionRepository;

    @Autowired
    private BranchRepository branchRepository;

    private final long GUEST_EXPIRATION = 2 * 30 * 24 * 60;
    private final long CASHIER_EXPIRATION = 2 * 30 * 24 * 60;


    @Override
    public UserDetails loadUserByUsername(String username) {
        // Kiểm tra xem user có tồn tại trong database không?
        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null) {
            throw new ApiException(MessageCode.USERNAME_NOTFOUND);
        }
        return user.toCustomUserDetails();
    }

    public MessageCode register(UserRegisterRequest request){
        User user = userRepository.findByUsername(request.getPhone()).orElse(null);
        if (user != null) {
            throw new ApiException(MessageCode.USERNAME_ALREADY_EXISTS);
        }
        Branch branch = branchRepository.getById(request.getBranchId()).orElse(null);
        if (branch == null && request.getRole().equals("USER"))throw new ApiException(MessageCode.ID_NOT_FOUND, "branchId = " + request.getBranchId());

        // gen code employee
        StringBuilder code;
        if (request.getRole().equals("ADMIN"))code = new StringBuilder();
        else {
            code = new StringBuilder(Utility.concatPrefixWord(branch.getName()));
            int countEmployee = userRepository.countEmployee(branch.getId());
            code.append(Utility.padWithZeros(String.valueOf(countEmployee), 6));
        }

        User newUser = new User();
        newUser.setUsername(request.getPhone());
        newUser.setCode(code.toString());
        newUser.setPhone(request.getPhone());
        newUser.setRole(request.getRole());
        newUser.setPositionId(request.getPositionId());
        newUser.setBranch(branch);
        newUser.setEmail(request.getEmail());
        newUser.setCreatedBy(request.getCreatedBy());
        newUser.setName(request.getName());
        newUser.setActive(1);
        newUser.setIsGuest(0);
        newUser.setPassword(passwordEncoder.encode(request.getPhone()));
        newUser.setIsGuest(0);
        newUser.setId(UUID.randomUUID().toString());
        // Set other fields of newUser as needed

        try {
            userRepository.save(newUser);
        }catch (Exception e) {
            throw new ApiException(MessageCode.FAIL);
        }
        return MessageCode.SUCCESS;
    }

    public User createGuestUser(String guestId) {
        User guestUser = new User();
        guestUser.setId(guestId);
        guestUser.setIsGuest(1);
        // Set other fields of guestUser as needed

        return userRepository.save(guestUser);
    }

    public String genToken(CustomUserDetails customUserDetails) {
        return jwtTokenUntil.generateToken(customUserDetails, CASHIER_EXPIRATION);
    }

    public AuthResponse genGuestToken(String code, Integer tableNumber){
        Branch branch = branchRepository.getByCodeAndActive(code, 1).orElse(null);
        if (branch == null)throw new ApiException(MessageCode.BRANCH_NOT_EXIST);

        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setIsGuest(1);
        user.setActive(1);
        user.setRole("GUEST");
        user.setBranch(branch);
        userRepository.save(user);
        AuthResponse response = new AuthResponse();
        Map<String, Object> payload = new HashMap<>();
        payload.put("branchId", branch.getId());
        payload.put("tableNumber", tableNumber);
        response.setJwt(jwtTokenUntil.generateToken(user.toCustomUserDetails(), GUEST_EXPIRATION, payload));
        response.setRole("GUEST");
        return response;
//        return null;
    }

    public MessageCode checkToken(TokenRequest request) {
        if (jwtTokenUntil.isTokenExpired(request.getJwt()))return MessageCode.TOKEN_EXPIRED;
        String userId = jwtTokenUntil.getClaimByKey(request.getJwt());
        System.out.println(userId);
        User user = userRepository.getByIdAndActive(userId, 1).orElse(null);
        if (user == null)return MessageCode.TOKEN_ERROR;
        return MessageCode.TOKEN_VALIDATE;
    }

    public List<EmployeeResponse> getEmployees(String userId) {
        List<User> userList = userRepository.getByAdminUserId(userId);
        return userList.stream().map(item -> {
            Position position = positionRepository.getById(item.getPositionId()).orElse(null);
            EmployeeResponse resItem = item.toEmployeeResponse();
            if (position == null)resItem.setPosition("");
            else
            resItem.setPosition(position.getName());
            return resItem;
        }).collect(Collectors.toList());
    }

    public EmployeeDetailsResponse getEmployeeDetails(String id, String userId) {
        User user = userRepository.getById(id).orElse(null);
        if (user == null)throw new ApiException(MessageCode.ID_NOT_FOUND, "UserId = " + id);
        if (!user.getCreatedBy().equals(userId))throw new ApiException(MessageCode.RESOURCES_AUTHORIZATION);
        return user.toEmployeeDetailsResponse();
    }

    public MessageCode updateEmployee(String userId, EmployeeUpdateRequest request) {
        User user = userRepository.getById(request.getId()).orElse(null);
        if (user == null)throw new ApiException(MessageCode.ID_NOT_FOUND, "UserId = " + request.getId());
        if (!user.getCreatedBy().equals(userId))throw new ApiException(MessageCode.RESOURCES_AUTHORIZATION);
        Branch branch = branchRepository.getById(request.getBranchId()).orElse(null);
        if (branch == null)throw new ApiException(MessageCode.ID_NOT_FOUND, "branchId = " + request.getBranchId());

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setActive(request.getStatus());
        user.setBranch(branch);
        user.setPositionId(request.getPositionId());
        user.setPhone(request.getPhone());

        try{
            userRepository.save(user);
            return MessageCode.SUCCESS;
        }
        catch (Exception e){
            throw new ApiException(MessageCode.FAIL, e);
        }
    }

    public MessageCode deleteEmployee(String id, String userId) {
        User user = userRepository.getById(id).orElse(null);
        if (user == null)throw new ApiException(MessageCode.ID_NOT_FOUND, "UserId = " + id);
        if (!user.getCreatedBy().equals(userId))throw new ApiException(MessageCode.RESOURCES_AUTHORIZATION);

        user.setActive(0);
        try {
            userRepository.save(user);
            return MessageCode.SUCCESS;
        }catch (Exception e){
            throw new ApiException(MessageCode.FAIL, e);
        }
    }

    public List<EmployeeResponse> getEmployeeManager(String userId) {
        List<User> userList = userRepository.getManagerUserList(userId);
        return userList.stream().map(User::toEmployeeResponse).collect(Collectors.toList());
    }
}
