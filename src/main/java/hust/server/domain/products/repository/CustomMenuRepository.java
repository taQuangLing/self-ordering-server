package hust.server.domain.products.repository;

import java.util.List;

public interface CustomMenuRepository {
    public List<Object> getMenuProducts(Long branchId);
}
