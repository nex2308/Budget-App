package pk.km.pasir_konieczny_mikolaj.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pk.km.pasir_konieczny_mikolaj.model.Membership;

import java.util.List;

@Repository
public interface MembershipRepository extends JpaRepository<Membership, Long> {
    List<Membership> findByGroupId(Long groupId);
    boolean existsByGroupIdAndUserId(Long groupId, Long userId);
    void deleteByGroupId(Long groupId);
}
