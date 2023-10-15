package com.ratz.wemanage.query;

public class UserQuery {

    public static final String COUNT_USER_EMAIL_QUERY = "SELECT COUNT(*) FROM Users WHERE email = :email";
    public static final String INSERT_USER_QUERY = "INSERT INTO users (first_name, last_name, email, password) VALUES (:firstName, :lastName, :email, :password)";
    public static final String INSERT_ACCOUNT_VERIFICATION_URL_QUERY = "INSERT INTO AccountVerifications (user_id, url) VALUES (:userId, :url)";
    public static final String SELECT_USER_BY_EMAIL_QUERY = "SELECT * FROM Users WHERE email = :email";
    public static final String DELETE_VERIFICATION_CODE_BY_USER_ID = "DELETE FROM twofactorverifications WHERE user_id = :id";
    public static final String INSERT_VERIFICATION_CODE_QUERY = "INSERT INTO twofactorverifications (user_id, code, expiration_date) VALUES (:userId, :code, :expirationDate)";

}
