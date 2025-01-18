package co.raccoons.bookkeeper.auth.signup;

import org.springframework.data.mongodb.repository.MongoRepository;

interface UserRepository extends MongoRepository<User, String> {
}
