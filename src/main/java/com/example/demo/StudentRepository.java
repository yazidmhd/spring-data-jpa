package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface StudentRepository extends JpaRepository<Student, Long> {
    //native query
    @Query(value="SELECT * FROM student WHERE first_name = :firstName AND age >= :age", nativeQuery = true)
    List<Student> selectStudentWhereFirstNameAndAgeGreaterThanEqualNative(
            @Param("firstName") String firstName,
            @Param("age") Integer age
    );

    //jpql
    @Query("select s from Student s where s.email = :email")
    Optional<Student> findStudentByEmail(@Param("email") String email);

    @Transactional
    @Modifying
    @Query("delete from Student u where u.id = ?1")
    int deleteStudentById(Long id);



    /*
    //query creation by @Query annotation
    //query will be executed therefore method name can be changed
    @Query("select s from Student s where s.email = ?1")
    Optional<Student> findStudentByEmail(String email);

    @Query("select s from Student s where s.firstName = ?1 and s.age >= ?2")
    List<Student> selectStudentWhereFirstNameAndAgeGreaterThanEqual(String firstName, Integer age);

    @Query("select s from Student s where s.firstName like ?1%")
    List<Student> findByFirstNameStartingWith(String letter);

    @Query("select s from Student s where s.firstName like %?1%")
    List<Student> findByFirstNameContaining(String word);

    @Query("select s from Student s where s.firstName not like %?1")
    List<Student> findByFirstNameEndingWithNotLike(String endLetter);

    @Query("select s from Student s where s.firstName not like ?1%")
    List<Student> findByFirstNameStartingWithNotLike(String startLetter);
     */

    /*
    //query creation by method names
    //search student by email only
    Optional<Student> findStudentByEmail(String email);

    //search student by firstName and age
    List<Student> findStudentByFirstNameEqualsAndAgeEquals(String firstName, Integer age);

    //search student by firstName ignore case and age is greater than equal
    List<Student> findStudentByFirstNameIgnoreCaseEqualsAndAgeGreaterThanEqual(String firstName, Integer age);

    Student findStudentByEmailAndAgeLessThanEqual(String email, Integer age);
    */


}
