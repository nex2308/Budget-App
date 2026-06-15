package pk.km.pasir_konieczny_mikolaj.exception;

public class UserAlreadyExistsExeption extends RuntimeException{
    public UserAlreadyExistsExeption(String message){
        super(message);
    }
}
