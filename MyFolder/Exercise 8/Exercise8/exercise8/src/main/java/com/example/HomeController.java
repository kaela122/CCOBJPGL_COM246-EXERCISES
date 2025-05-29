package com.example;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;


public class HomeController implements Initializable {
    
    ObservableList<User> mylist = FXCollections.observableArrayList();

    @FXML
    Label usernamelabel;
  
    @FXML
    public  Button DeleteButton;

    @FXML
    public Button UpdateButton;

    @FXML
    public Button CreateButton;

    @FXML
    public TableColumn<User, String> Usercolumn;

    @FXML
    public TableColumn<User, String> createdcolumn;

    @FXML
    public TableColumn<User, String> passwordcolumn;

    @FXML
    public TextField passwordtextfield;

    @FXML
    private TableView<User> table;

    @FXML
    public ChoiceBox<String> statuschoicebox;

    @FXML
    public TableColumn<User, String> statuscolumn;

    @FXML
    public  TextField usernametextfield;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeCol();
        loadData();

        // Add items to choice box
        statuschoicebox.getItems().addAll("Inactive", "Active");

        // TODO Auto-generated method stub
        String username = LoginController.user.getUsername();
        usernamelabel.setText(username);
    } 

    private void initializeCol(){
        Usercolumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        passwordcolumn.setCellValueFactory(new PropertyValueFactory<>("password"));
        createdcolumn.setCellValueFactory(new PropertyValueFactory<>("accountcreated"));
        statuscolumn.setCellValueFactory(new PropertyValueFactory<>("accountstatus"));
    }
    private void loadData(){

        mylist.clear();

        try {
            // Create object from File class
            File myFile = new File("accounts.txt");
        

            // .exists() method checks if a file exists in the pathname
            if (myFile.exists()) {

                Scanner fileScanner = new Scanner(myFile);

                while (fileScanner.hasNextLine()) {

                    String data = fileScanner.nextLine();
        
                    String username = data.split(",")[0];
                    String password = data.split(",")[1];
                    String dcreated = data.split(",")[2];
                    String status = data.split(",")[3];

                    mylist.add(new User(username, password, dcreated, status));
                } 
                    table.setItems(mylist);

                fileScanner.close();
            }
            else {
                System.out.println(myFile.getName() + " does not exist!");
            }
        } catch (Exception e) {
            System.out.println("There is an error");
        }
    }


        @FXML
    private boolean createuser(ActionEvent event) {

        String username = usernametextfield.getText();

        String password = passwordtextfield.getText();

        String status = statuschoicebox.getValue();

        System.out.println(status);
        username = username.trim();
        password = password.trim();
        status = status.trim();

        if(username.length() == 0)
        {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setContentText("no username provided");
        }

        if(password.length() == 0)
        {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setContentText("no password provided");
        }

        // Get current date
        LocalDate today = LocalDate.now();

        // Format as MM-dd-yyyy
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        String formattedDate = today.format(formatter);

        // Create user object
        User user = new User(username, password, formattedDate, status);

        try {

            BufferedWriter myWriter = new BufferedWriter(new FileWriter("accounts.txt", true));
      
            // .write() methods adds content to the file
            myWriter.newLine(); // adds a new line
            myWriter.write(user.getUsername() + "," + user.getPassword() + "," + user.getAccountcreated() + "," + user.getAccountstatus());

            // Close FileWriter
            myWriter.close();

            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText("Account Created");
            alert.setContentText("You have created a new account!");
            alert.showAndWait();
            
            // Refresh data in table
            loadData();

        } catch (IOException e) {
            System.out.println("An error occurred.");
        }

        return true;
    }
    
    @FXML  
    public boolean deleteuser(ActionEvent event) {

        // Get selected row from table
        User user = table.getSelectionModel().getSelectedItem();

        String username = (user.getUsername());

        System.out.println(username);

        //String filename = "accounts.txt";
        String userToDelete = username;

        List<String> updatedLines = new ArrayList<>();

        // Step 1: Read and filter lines
        try (BufferedReader reader = new BufferedReader(new FileReader("accounts.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) { // skip empty lines
                    String[] parts = line.split(",");
                    if (!parts[0].equalsIgnoreCase(userToDelete)) {
                        updatedLines.add(line);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        // Step 2: Write back without trailing newline
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("accounts.txt"))) {
            for (int i = 0; i < updatedLines.size(); i++) {
                writer.write(updatedLines.get(i));
                if (i < updatedLines.size() - 1) {
                    writer.newLine(); // add newline except after the last line
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText("This is the header");
        alert.setContentText("User '" + userToDelete + "' has been deleted (if existed).");
        alert.showAndWait();        
        loadData();

        return true;
    }
    
    @FXML
    public boolean updateuser(ActionEvent event) {

        User user = table.getSelectionModel().getSelectedItem();

        String username = usernametextfield.getText();

        String password = passwordtextfield.getText();

        String status = statuschoicebox.getValue();

        username = username.trim();
        password = password.trim();
        status = status.trim();

        if(username.length() == 0)
        {
            System.out.println("No username!");
            return false;
        }

        if(password.length() == 0)
        {
            System.out.println("No password!");
            return false;
        }

        String targetUsername = user.getUsername();
        String newPassword = password;
        String newStatus = status;
        String newUsername = username;

        List<String> updatedLines = new ArrayList<>();

        // Step 1: Read and update
        try (BufferedReader reader = new BufferedReader(new FileReader("accounts.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    String[] parts = line.split(",");

                    if (parts.length == 4 && parts[0].equalsIgnoreCase(targetUsername)) {
                        updatedLines.add(newUsername + "," + newPassword + "," + user.getAccountcreated() + "," + newStatus);
                    } else {
                        updatedLines.add(line);
                    }

                    // if (parts.length == 2 && parts[0].equalsIgnoreCase(targetUsername)) {
                    //     // Update password
                    //     updatedLines.add(parts[0] + "," + newPassword + "," + newStatus);
                    // } else {
                    //     updatedLines.add(line);
                    }
                }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        // Step 2: Write updated lines back
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("accounts.txt"))) {
            for (int i = 0; i < updatedLines.size(); i++) {
                writer.write(updatedLines.get(i));
                if (i < updatedLines.size() - 1) {
                    writer.newLine(); // no extra blank line
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText("This is the header");
        alert.setContentText("User '" + targetUsername + "' has been updated.");
        alert.showAndWait();
        loadData();
        return true;
    }
}

   

    
