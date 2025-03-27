import java.io.File;
import java.util.Scanner;

public class App {
    public static void main(String[] args) throws Exception {
  
        System.out.println("Enter your username:");

         Scanner loginScanner = new Scanner(System.in);

        String username_from_input = loginScanner.nextLine();

        System.out.println("Enter your password:");

        String password_from_input = loginScanner.nextLine();

        // System.out.println("Your username is:" + username_from_input);
        // System.out.println("Your password is:" + password_from_input);

        // Declare file object
        File myfile = new File("account.txt");

        // Check if file exists
        if (myfile.exists()) {
            System.out.println("File exists");
        }
        // Read file
        Scanner fileScanner = new Scanner(myfile);
       
        boolean accountExist = false;
        while (fileScanner.hasNextLine()) {
                 String filedata = fileScanner.nextLine();
                 String username_from_file = filedata.split(",")[0];
                 String password_from_file = filedata.split(",")[1];
        if (username_from_input.equals(username_from_file) && password_from_input.equals(password_from_file)) { 
            accountExist = true;
            System.out.println("Log-in Successfully ");
            break;
        }
        // System.out.println(Filedata)

    }

             if (accountExist == false) {
            System.err.println("Login Failed");
             }

    }
}
