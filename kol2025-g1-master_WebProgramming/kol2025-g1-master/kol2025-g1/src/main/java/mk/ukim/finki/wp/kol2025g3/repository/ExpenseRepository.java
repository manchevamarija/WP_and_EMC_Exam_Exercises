package mk.ukim.finki.wp.kol2025g3.repository;


import mk.ukim.finki.wp.kol2025g3.model.Expense;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpenseRepository extends JpaSpecificationRepository<Expense,Long> {
}
