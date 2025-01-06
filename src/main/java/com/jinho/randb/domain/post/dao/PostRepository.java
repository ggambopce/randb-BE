package com.jinho.randb.domain.post.dao;

import com.jinho.randb.domain.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post,Long>,CustomPostRepository{

    @Modifying
    @Query("delete from Post c where c.account.id =:account_id and c.id =:post_id")
    void deleteAccountId(@Param("account_id")Long account_id, @Param("post_id")Long post_id);

    long countAllBy();
}
