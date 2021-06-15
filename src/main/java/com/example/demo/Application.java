package com.example.demo;

import com.github.javafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    // Have code run after the startup
    // inject StudentRepository -> need to have @Bean annotation
    @Bean
    CommandLineRunner commandLineRunner(
            StudentRepository studentRepository,
            StudentIdCardRepository studentIdCardRepository) {
        return args -> {
            //generate fake student
            Faker faker = new Faker();
            String firstName = faker.name().firstName();
            String lastName = faker.name().lastName();
            String email = String.format("%s.%s@gmail.com", firstName, lastName);
            Student student = new Student(
                    firstName,
                    lastName,
                    email,
                    faker.number().numberBetween(17, 55)
            );

            student.addBook(new Book(
                "Clean code", LocalDateTime.now().minusDays(4)
            ));

            student.addBook(new Book(
                    "Thinking in Design", LocalDateTime.now()
            ));

            student.addBook(new Book(
                    "Happier Times", LocalDateTime.now().minusYears(1)
            ));

            StudentIdCard studentIdCard = new StudentIdCard("123456789", student);
            student.setStudentIdCard(studentIdCard);

            student.addEnrolment(new Enrolment(
                    new EnrolmentId(1L, 1L),
                    student,
                    new Course(
                    "Computer Science",
                    "IT"
                    ),
                    LocalDateTime.now()
            ));

            student.addEnrolment(new Enrolment(
                    new EnrolmentId(1L, 2L),
                    student,
                    new Course(
                    "Intro to Marketing",
                    "Business Development"
                    ),
                    LocalDateTime.now().minusDays(18)
            ));


            studentRepository.save(student);

            studentRepository.findById(1L)
                    .ifPresent(s -> {
                        System.out.println("fetch books eager... ");
                        List<Book> books = student.getBooks();
                        books.forEach(book -> {
                            System.out.println(s.getFirstName() + " borrowed " + book.getBookName());
                        });
                    });

//            studentRepository.deleteById(1L);

        };
    }

    private void generateRandomStudents(StudentRepository studentRepository) {
        //initialise Faker
        Faker faker = new Faker();
        for (int i = 0; i < 20; i++) {
            //generate new Student and persist it into the database
            String firstName = faker.name().firstName();
            String lastName = faker.name().lastName();
            String email = String.format("%s.%s@gmail.com", firstName, lastName);
            Student student = new Student(
                    firstName,
                    lastName,
                    email,
                    faker.number().numberBetween(17, 55)
            );
            studentRepository.save(student);
        }
    }

    private void pagination(StudentRepository studentRepository) {
        //1st param = the page you're at/0 page = first page
        //2nd param = how many you would want to return from the database
        //you can also sort the number of data returned back to you as the third param
        //the arguments are normally passed down from the client and then return back the page
        PageRequest pageRequest = PageRequest.of(
                0,
                5,
                Sort.by("firstName").ascending());
        Page<Student> page = studentRepository.findAll(pageRequest);
        System.out.println(page);
    }

    private void sorting(StudentRepository studentRepository) {
        //sort by firstname in ascending order
        //1st way to do sorting - by implementing it as a parameter its direction
        Sort sortOne = Sort.by(Sort.Direction.DESC, "firstName");

        //2nd way to do sorting - by appending the direction
        //this allows you to chain the sorting with .and() -> first by name then by age
        Sort sortTwo = Sort.by("firstName").ascending().and(Sort.by("age").descending());

        //get all students and sort them by first name asc and age desc
        studentRepository.findAll(sortTwo).forEach(
                student -> System.out.println(student.getFirstName() + " " + student.getAge())
        );
    }



}
