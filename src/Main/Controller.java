package Main;

import Main.Model.ThreadClient;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Circle;


import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

public class Controller implements  Observer, Initializable {
    private Socket socket;
    private DataOutputStream bufferDeSalida = null;
    private int wins=0;
    private int loses=0;
    private ArrayList<Label> labels= new ArrayList<>();
    int matriz[][]={
            {0,1,2},
            {3,4,5},
            {6,7,8},
            {0,3,6},
            {1,4,7},
            {2,5,8},
            {0,4,8},
            {2,4,6}
    };
    private boolean turno=true;
    private boolean ok=true;

    @FXML
    private Label labelConectado;
    @FXML
    private Label labelEsperar;
    @FXML
    private Label labelWins;
    @FXML
    private Label labelLoses;
    @FXML
    private Button btnSalir;
    @FXML
    private Button btnReiniciar;
    @FXML
    private Button btnConectar;
    @FXML
    private GridPane grid;
    @FXML
    private Label labelOfName;
    @FXML
    private Label label0;
    @FXML
    private Label label1;
    @FXML
    private Label label3;
    @FXML
    private Label label2;
    @FXML
    private Label label4;
    @FXML
    private Label label5;
    @FXML
    private Label label6;
    @FXML
    private Label label7;
    @FXML
    private Label label8;

    @FXML
    void reiniciarOnMouseClicked(MouseEvent event) {
        reiniciar();
        try {
            bufferDeSalida.writeUTF("Nodo1-"+"X:"+"10");
            bufferDeSalida.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reiniciar(){
        for (int x=0; x<labels.size(); x++){
            labels.get(x).setText("");
            labels.get(x).setStyle("-fx-background-color: black; -fx-border-color: white; -fx-border-radius: 5;");
        }
        labelEsperar.setText("");
        turno=true;
        ok=true;
    }

    @FXML
    void btnConectarOnMouseClicked(MouseEvent event) {
        conectar();
    }

    public boolean casillaLibre(Label l){
        if(l.getText().equals("")){
            return true;
        }
        else{
            return false;
        }
    }

    public void comprobarGanador(){
        for (int x=0; x<matriz.length; x++){
            if (labels.get(matriz[x][0]).getText().equals("X") &&
                labels.get(matriz[x][1]).getText().equals("X") &&
                labels.get(matriz[x][2]).getText().equals("X")){
                    wins++;
                    String w= String.valueOf(wins);
                    labelWins.setText(w);
                    ok=false;
                    labels.get(matriz[x][0]).setStyle("-fx-background-color: green; -fx-border-color: white; -fx-border-radius: 5;");
                    labels.get(matriz[x][1]).setStyle("-fx-background-color: green; -fx-border-color: white; -fx-border-radius: 5;");
                    labels.get(matriz[x][2]).setStyle("-fx-background-color: green; -fx-border-color: white; -fx-border-radius: 5;");

            }
            if (labels.get(matriz[x][0]).getText().equals("O") &&
                labels.get(matriz[x][1]).getText().equals("O") &&
                labels.get(matriz[x][2]).getText().equals("O")){
                    loses++;
                    String l= String.valueOf(loses);
                    labelLoses.setText(l);
                    ok=false;
                    labels.get(matriz[x][0]).setStyle("-fx-background-color: red; -fx-border-color: white; -fx-border-radius: 5;");
                    labels.get(matriz[x][1]).setStyle("-fx-background-color: red; -fx-border-color: white; -fx-border-radius: 5;");
                    labels.get(matriz[x][2]).setStyle("-fx-background-color: red; -fx-border-color: white; -fx-border-radius: 5;");
            }
        }
    }
    public void presionarCasilla(Label label, int posicion){
        String idx= String.valueOf(posicion);
        if(ok){
            if(casillaLibre(label) && turno){
                label.setText("X");
                try {
                    bufferDeSalida.writeUTF("Nodo1-"+"X:"+idx);
                    bufferDeSalida.flush();
                    turno= false;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                comprobarGanador();
            }else{
                labelEsperar.setText("Espera tu turno");
            }
        }else{
            labelEsperar.setText("Dale reiniciar.");
        }

    }

    @FXML
    void label0OnMouseClicked(MouseEvent event) {
        presionarCasilla( label0, 0);
    }

    @FXML
    void label1OnMouseClicked(MouseEvent event) {
        presionarCasilla( label1, 1);
    }

    @FXML
    void label2OnMouseClicked(MouseEvent event) {
        presionarCasilla( label2, 2);
    }

    @FXML
    void label3OnMouseClicked(MouseEvent event) {
        presionarCasilla( label3, 3);
    }

    @FXML
    void label4OnMouseClicked(MouseEvent event) {
        presionarCasilla( label4, 4);
    }

    @FXML
    void label5OnMouseClicked(MouseEvent event) {
        presionarCasilla( label5, 5);
    }

    @FXML
    void label6OnMouseClicked(MouseEvent event) {
        presionarCasilla( label6, 6);
    }

    @FXML
    void label7OnMouseClicked(MouseEvent event) {
        presionarCasilla( label7, 7);
    }

    @FXML
    void label8OnMouseClicked(MouseEvent event) {
        presionarCasilla( label8, 8);
    }

    void conectar(){
        try {
            socket = new Socket("192.168.1.68", 3001);
            labelConectado.setText( "Conectado...");
            bufferDeSalida = new DataOutputStream(socket.getOutputStream());
            bufferDeSalida.flush();
            ThreadClient cliente = new ThreadClient(socket);
            cliente.addObserver(this);
            new Thread(cliente).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void btnSalirOnMouseClicked(MouseEvent event) {
        Platform.exit();
        System.exit(1);
    }

    @FXML
    void btnCerrarOnMouseClicked(MouseEvent event) {
        try {
            socket.close();
            System.out.println("Cerrando...");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void update(Observable o, Object arg) {
        String pos = (String) arg;
        int position = Integer.parseInt(pos);
        if (position!=10){
            Platform.runLater(() -> labels.get(position).setText("O"));
            Platform.runLater(()-> comprobarGanador());
            Platform.runLater(()-> labelEsperar.setText(""));
        }else{
            Platform.runLater(()-> reiniciar());
            Platform.runLater(()-> labelEsperar.setText(""));
        }
        turno=true;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        labels.add(label0);
        labels.add(label1);
        labels.add(label2);
        labels.add(label3);
        labels.add(label4);
        labels.add(label5);
        labels.add(label6);
        labels.add(label7);
        labels.add(label8);
    }
}
