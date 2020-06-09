package engset.calculator;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class controller implements Initializable {

    boolean rhoOrOff = true; //true = rho
    
    @FXML
    Tab engsetTab, erlangTab;   
    @FXML
    RadioButton rRadio0, pbRadio0, rRadio1, pbRadio1;
    @FXML
    TextField rhoField0, nField0, rField0, offField0, pbField0,
            rhoField1, rField1, pbField1;
    @FXML
    TextArea resultArea0;
    @FXML
    TextArea resultArea1;   
   
            
    @Override
    public void initialize(URL url, ResourceBundle rb) {       
        resultArea0.setWrapText(true);
        resultArea1.setWrapText(true);     
    }         
    
    @FXML
    public void calcEngset(){        
        if (pbRadio0.isSelected()){
            int n = 0, r = 0;
            try {
                    n = Integer.parseInt(nField0.getText());
            }
            catch (Exception e){
                resultArea0.setText("Please give an integer as the number of sources!");
                return;
            }
            try {
                r = Integer.parseInt(rField0.getText());
            }
            catch (Exception e){
                resultArea0.setText("Please give an integer as the number of servers!");
                return;
            }
            if (rhoOrOff){
                double rho = 0;                
                try {
                    rho = Double.valueOf(rhoField0.getText());
                }
                catch (Exception e){
                    resultArea0.setText("Please give a number as Rho!");
                    return;
                }                    
                resultArea0.setText(String.format("Pb = %f", Engset(n, r, rho)));   
                
            }
            else{
                double offered = 0;                
                try {
                    offered = Double.valueOf(offField0.getText());
                }
                catch (Exception e){
                    resultArea0.setText("Please give a number as offered traffic!");
                    return;
                }                
                resultArea0.setText("Calculating...");
                double ret = approximatePb(n, r, offered);
                if (ret == -1 || ret == Double.NaN)
                    resultArea0.setText("Pb does not converge.");
                else
                    resultArea0.setText(String.format("Pb = %f", ret));                    
            }
        }
        else if (rRadio0.isSelected()){
            int n;
            double Pb;
             try {
                    n = Integer.parseInt(nField0.getText());
            }
            catch (Exception e){
                resultArea0.setText("Please give an integer as the number of sources!");
                return;
            }
            try {
                Pb = Double.valueOf(pbField0.getText());                
            }
            catch (Exception e){
                resultArea0.setText("Please give a number as Pb!");
                return;
            }
            if (rhoOrOff){
                double rho = 0;                
                try {
                    rho = Double.valueOf(rhoField0.getText());
                }
                catch (Exception e){
                    resultArea0.setText("Please give a number as Rho!");
                    return;
                } 
                int r = 1;
                double act = Engset(n, r, rho);
                resultArea0.clear();
                resultArea0.appendText(String.format("r = %d\tPb = %f\n", r, act));
                while(act > Pb){
                    r++;
                    act = Engset(n, r, rho);                    
                    resultArea0.appendText(String.format("r = %d\tPb = %f\n", r, act));
                }
            }
            else{
                double offered = 0;                
                try {
                    offered = Double.valueOf(offField0.getText());
                }
                catch (Exception e){
                    resultArea0.setText("Please give a number as offered traffic!");
                    return;
                }                
                resultArea0.clear();
                int r = 0;
                double act = 1;
                do {
                    r++;
                    act = approximatePb(n, r, offered);
                    if (act == -1 || act == Double.NaN){
                       resultArea0.setText("Pb does not converge.");
                       return;
                    }                    
                    else
                        resultArea0.appendText(String.format("r = %d\t\tPb = %f\n", r, act));                    
                }
                while (act > Pb);                                  
            } 
        }
            
        else {
            resultArea0.setText("Please choose the value to be calculated!");
        }        
    }
    
    @FXML
    public void calcErlang(){
        double rho = 0;
        try{
            rho = Double.valueOf(rhoField1.getText());
        }
        catch (Exception e){
            resultArea1.setText("Please give a number as Rho!");
            return;
        }
        if (pbRadio1.isSelected()){
            int r = 0;
            try {
                r = Integer.parseInt(rField1.getText());
            } catch (Exception e) {
                resultArea1.setText("Please give an integer as the number of servers!");
                return;
            }
            resultArea1.setText(String.format("Pb = %f", Erlang(r, rho)));
        }
        else if (rRadio1.isSelected()){
            double Pb = 1;
            try{
                Pb = Double.valueOf(pbField1.getText());
            }
            catch (Exception e){
                resultArea1.setText("Please give a number as Pb!");
                return;
            }
            int r = 0;
            double act = 1;
            resultArea1.clear();;
            while(act > Pb){
                r++;
                act = Erlang(r, rho);                
                resultArea1.appendText(String.format("r = %d\t\tPb = %f\n", r, act));
            }            
        }
        else
            resultArea1.setText("Please choose the value to be calculated!");
    }
    
    @FXML
    public void rRadio0selected(){
        rField0.setDisable(true);
        pbField0.setDisable(false);
        pbRadio0.setSelected(false);
    }
    
    @FXML
    public void pbRadio0selected(){
        pbField0.setDisable(true);
        rField0.setDisable(false);
        rRadio0.setSelected(false);
    } 
    
    @FXML
    public void rRadio1selected(){
        rField1.setDisable(true);
        pbField1.setDisable(false);
        pbRadio1.setSelected(false);
    }
    
    @FXML
    public void pbRadio1selected(){
        pbField1.setDisable(true);
        rField1.setDisable(false);
        rRadio1.setSelected(false);
    }
    
    @FXML
    public void rhoField0selected(){        
        offField0.setStyle("-fx-background-color: rgb(244,244,244)");       
        rhoField0.setStyle(null);
        rhoOrOff = true;
    }
    
    @FXML
    public void offField0selected(){       
        rhoField0.setStyle("-fx-background-color: rgb(244,244,244)");
        offField0.setStyle(null);
        rhoOrOff = false;
    }
    
    @FXML
    public void engsetChose(){
        resultArea0.setText("Use the radio buttons to choose the value to be calculated!\n\n" + 
            "If the 'Number of servers' field is chosen, the maximum blocking probability must be given in 'Blocking probability' field.\n" +
            "If the 'Blocking probability' field is chosen, the 'Number of servers' field must be filled.\n\n" +
            "'Number of servers' field and one of the 'Rho' and 'Offered trafic' fields must be filled in any case.\n"); 
        rhoField0.setText("");
        pbField0.setText("");
        rField0.setText(""); 
        offField0.setText("");
        nField0.setText("");
    }
    
    @FXML
    public void erlangChose(){
        resultArea1.setText("Use the radio buttons to choose the value to be calculated\n\nThe other values must be given.\n\n" +
                "If the 'Number of servers' field is chosen, the blocking probability refers to the maximum acceptable value.");       
        rhoField1.setText("");
        pbField1.setText("");
        rField1.setText("");        
    }
    
    public static double approximatePb(int n, int r, double offered){
        double Pb = 0.5, prev_Pb = 0;    
        double alpha = offered/n;
        long starttime = System.currentTimeMillis();
        while (Math.abs(Pb - prev_Pb) > 0.0001){
                prev_Pb = Pb;                
                double rho = alpha/(1 - alpha*(1-Pb));
                Pb = Engset(n, r, rho);   
                if (System.currentTimeMillis()- starttime > 1000)
                    return -1;
        }         
        return Pb;
    }
    
    public static double Erlang(int r, double rho){
	if (r == 1){
		return rho / (rho+1);
	}
	double b = Erlang(r-1, rho);
	return rho*b / (r+rho*b);
    }
    
    public static double Engset(int n, int r, double rho){
	if (r == 1){
		return ((n-1)*rho) / (1+(n-1)*rho);
	}
	double e = Engset(n, r-1, rho);
	return ((n-r)*rho*e) / (r+(n-r)*rho*e);	
    } 
        
}
