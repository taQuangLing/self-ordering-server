package hust.server.domain.products.repository.impl;

import hust.server.domain.products.repository.CustomMenuRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomMenuRepositoryImpl implements CustomMenuRepository {
    @PersistenceContext
    private EntityManager entityManager;
    @Override
    public List<Object> getMenuProducts(Long branchId){
        StringBuilder sql = new StringBuilder();
        sql.append( "select p.id as productId, p.name as productName, c.id as categoryId, c.name as categoryName from products p, categories c," +
                    "(select menu_item.id as id from menu, menu_item " +
                    "where " +
                    "menu.branch_id = :branchId and " +
                    "menu_item.menu_id = menu.id and " +
                    "                menu.active = 1 and " +
                    "                menu_item.active = 1 " +
                    ") menu_p " +
                    "where  " +
                    " p.category_id = c.id and " +
                    "    p.active = 1 and c.active = 1 and " +
                    "    p.id = menu_p.id");

        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);

        Query query = entityManager.createNativeQuery(sql.toString());
        params.forEach(query::setParameter);
        List<Object> menuGuest =  query.getResultList();
        return menuGuest;
    }
}
