import javafx.application.Application;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class MainApp extends Application {

    BookingManager manager = new BookingManager();

    @Override
    public void start(Stage stage){
        showLogin(stage);
    }

    void showLogin(Stage stage){

        TextField user = new TextField();
        user.setPromptText("Username");

        PasswordField pass = new PasswordField();
        pass.setPromptText("Password");

        Button login = new Button("Login");

        Label msg = new Label();

        login.setOnAction(e -> {
            if(user.getText().equals("subrat") && pass.getText().equals("123")){
                stage.setScene(mainScene());
            } else {
                msg.setText("Invalid Login");
            }
        });

        VBox v = new VBox(10, user, pass, login, msg);
        v.setAlignment(Pos.CENTER);

        stage.setScene(new Scene(v,300,200));
        stage.setTitle("Login");
        stage.show();
    }

    Scene mainScene(){

        manager.loadFromFile();

        TabPane tabs = new TabPane();

        tabs.getTabs().add(addRoomTab());
        tabs.getTabs().add(bookingTab());
        tabs.getTabs().add(customerTab());
        tabs.getTabs().add(viewTab());

        return new Scene(tabs,900,600);
    }

    Tab addRoomTab(){

        GridPane g = new GridPane();
        g.setHgap(10);
        g.setVgap(10);
        g.setPadding(new Insets(20));

        TextField room = new TextField();
        ComboBox<String> type = new ComboBox<>();
        type.getItems().addAll("Single","Double","Deluxe");

        TextField price = new TextField();

        Button add = new Button("Add Room");

        add.setOnAction(e -> {
            manager.addRoom(new Room(
                Integer.parseInt(room.getText()),
                type.getValue(),
                Double.parseDouble(price.getText())
            ));
            new Alert(Alert.AlertType.INFORMATION,"Room Added").show();
        });

        g.add(new Label("Room Number"),0,0);
        g.add(room,1,0);

        g.add(new Label("Room Type"),0,1);
        g.add(type,1,1);

        g.add(new Label("Price"),0,2);
        g.add(price,1,2);

        g.add(add,1,3);

        return new Tab("Add Room", g);
    }

    Tab bookingTab(){

        TextField room = new TextField();
        TextField name = new TextField();
        TextField contact = new TextField();
        TextField days = new TextField();

        Button book = new Button("Book Room");

        book.setOnAction(e -> {
            if(manager.bookRoom(
                Integer.parseInt(room.getText()),
                name.getText(),
                contact.getText()
            )){
                double bill = manager.calculateBill(
                    Integer.parseInt(room.getText()),
                    Integer.parseInt(days.getText())
                );

                new Alert(Alert.AlertType.INFORMATION,
                        "Booking Successful\nTotal Bill: " + bill).show();
            } else {
                new Alert(Alert.AlertType.ERROR,"Room Not Available").show();
            }
        });

        VBox v = new VBox(10);

        v.getChildren().addAll(
            new Label("Room Number"), room,
            new Label("Customer Name"), name,
            new Label("Contact Number"), contact,
            new Label("Number of Days"), days,
            book
        );

        v.setPadding(new Insets(20));

        return new Tab("Booking", v);
    }

    Tab customerTab(){

        TextArea area = new TextArea();

        Button show = new Button("Show Customers");

        show.setOnAction(e -> {
            String text = "";

            for(Customer c: manager.getCustomers()){
                text += "Room " + c.getRoomNo() + " - " + c.getName() + "\n";
            }

            area.setText(text);
        });

        VBox v = new VBox(10, show, area);
        v.setPadding(new Insets(20));

        return new Tab("Customers", v);
    }

    Tab viewTab(){

        TableView<Room> table = new TableView<>();

        TableColumn<Room,Integer> c1 = new TableColumn<>("Room No");
        c1.setCellValueFactory(x ->
            new javafx.beans.property.SimpleIntegerProperty(x.getValue().getRoomNo()).asObject()
        );

        TableColumn<Room,String> c2 = new TableColumn<>("Type");
        c2.setCellValueFactory(x ->
            new javafx.beans.property.SimpleStringProperty(x.getValue().getType())
        );

        TableColumn<Room,Double> c3 = new TableColumn<>("Price");
        c3.setCellValueFactory(x ->
            new javafx.beans.property.SimpleDoubleProperty(x.getValue().getPrice()).asObject()
        );

        TableColumn<Room,String> c4 = new TableColumn<>("Status");
        c4.setCellValueFactory(x ->
            new javafx.beans.property.SimpleStringProperty(
                x.getValue().isAvailable() ? "Available" : "Booked"
            )
        );

        table.getColumns().addAll(c1,c2,c3,c4);

        Button load = new Button("Load Rooms");

        load.setOnAction(e -> {
            table.getItems().setAll(manager.getRooms());
        });

        VBox v = new VBox(10, load, table);
        v.setPadding(new Insets(20));

        return new Tab("Rooms", v);
    }

    public static void main(String[] args){
        launch(args);
    }
}