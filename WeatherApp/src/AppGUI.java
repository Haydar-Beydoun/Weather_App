import org.json.simple.JSONObject;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class AppGUI extends JFrame{
    JPanel cardPanel = new JPanel(new CardLayout());
    HomePanel homePanel = new HomePanel();
    SettingsPanel settingsPanel = new SettingsPanel();
    CardLayout cl = new CardLayout();


    public AppGUI(){
        //App name
        super("Weather Monkey");

        //exit app when closing program
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        //Setting size of GUI
        setSize(450, 700);

        //preventing resize
        setResizable(false);

        //Center the GUI
        setLocationRelativeTo(null);

        //manage GUI interface location
        setLayout(cl);
        cardPanel.setLayout(cl);
        homePanel.setLayout(null);
        settingsPanel.setLayout(null);

        cardPanel.add(homePanel, "1");
        cardPanel.add(settingsPanel, "2");
        cl.show(cardPanel, "1");

        add(cardPanel);
    }

    class HomePanel extends JPanel{
        JTextField citySearchField;
        JTextField provinceSearchField;
        JTextField countrySearchField;
        JButton searchButton;
        JButton settingsButton;
        JLabel weatherConditionImage;
        JLabel weatherConditionText;
        JLabel windSpeedImage;
        JLabel windSpeedText;
        JLabel temperatureText;
        JLabel humidityImage;
        JLabel humidityText;
        JLabel locationText;
        JLabel background;
        double temperature;
        boolean inFahrenheit = false;
        boolean displayingCity = false;
        Sound errorSound = new Sound();
        Sound music = new Sound();

        HomePanel(){
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            //Sound
            errorSound.setSound(getClass().getResource("assets/error.wav"));
            music.setSound(getClass().getResource("assets/music.wav"));
            music.loop();
            music.play();

            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            //City Searchbox
            citySearchField = new JTextField();

            //Set location and size of search box
            citySearchField.setOpaque(false);

            Color highlightColor = new Color(133, 212, 255);
            Color shadowColor = new Color(12, 130, 194);
            citySearchField.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED, highlightColor , shadowColor));

            citySearchField.setBounds(20, 40, 110, 30);

            //Set the font of seach box text
            citySearchField.setFont(new Font("Arial", Font.PLAIN, 16));

            add(citySearchField);

            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            //province Searchbox
             provinceSearchField = new JTextField();

            //Set location and size of search box
            provinceSearchField.setOpaque(false);
            provinceSearchField.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED, highlightColor , shadowColor));
            provinceSearchField.setBounds(140, 40, 110, 30);

            //Set the font of seach box text
            provinceSearchField.setFont(new Font("Arial", Font.PLAIN, 16));

            add(provinceSearchField);

            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            //Country Searchbox
            countrySearchField = new JTextField();

            //Set location and size of search box
            countrySearchField.setOpaque(false);
            countrySearchField.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED, highlightColor , shadowColor));
            countrySearchField.setBounds(260, 40, 110, 30);

            //Set the font of seach box text
            countrySearchField.setFont(new Font("Arial", Font.PLAIN, 16));

            add(countrySearchField);

            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            //Search Button
            searchButton = new JButton(loadScaledImage("assets/search.png", 25, 25));

            searchButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            searchButton.setBounds(385, 40, 30, 30);
            searchButton.setContentAreaFilled(false);
            searchButton.setBorderPainted(false);

            add(searchButton);

            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            //Settings Button
            settingsButton = new JButton(loadScaledImage("assets/settings.png", 25, 25));

            settingsButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            settingsButton.setBounds(400, 0, 30, 30);
            settingsButton.setContentAreaFilled(false);
            settingsButton.setBorderPainted(false);

            add(settingsButton);

            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            //Weather Image
            weatherConditionImage = new JLabel(loadScaledImage("assets/logo.png", 210, 210));

            weatherConditionImage.setBounds(0, 100, 450, 217);

            add(weatherConditionImage);

            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            //Temperature Text
            temperatureText = new JLabel();

            temperatureText.setBounds(0, 325, 450, 30);
            temperatureText.setHorizontalAlignment(SwingConstants.HORIZONTAL);

            temperatureText.setFont(new Font("Arial", Font.BOLD, 32));

            add(temperatureText);

            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            //Weather Condition Text
            weatherConditionText = new JLabel("Weather Monkey");

            weatherConditionText.setBounds(0, 375, 450, 30);
            weatherConditionText.setHorizontalAlignment(SwingConstants.HORIZONTAL);

            weatherConditionText.setFont(new Font("Arial", Font.PLAIN, 32));

            add(weatherConditionText);

            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            //Humidity Image
            humidityImage = new JLabel(loadImage("assets/humidity.png"));

            humidityImage.setBounds(20, 475, 74, 66);

            add(humidityImage);

            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            //Humidity Text
            humidityText = new JLabel();

            humidityText.setBounds(110, 490, 450, 50);

            humidityText.setFont(new Font("Arial", Font.PLAIN, 18));

            add(humidityText);

            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            //Wind speed Image
            windSpeedImage = new JLabel(loadImage("assets/windspeed.png"));

            windSpeedImage.setBounds(20, 575, 74, 66);

            add(windSpeedImage);

            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            //Wind speed Text
            windSpeedText = new JLabel();

            windSpeedText.setBounds(110, 580, 450, 50);

            windSpeedText.setFont(new Font("Arial", Font.PLAIN, 18));

            add(windSpeedText);

            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            //Location Text
            locationText = new JLabel();

            locationText.setBounds(0, 400, 450, 50);
            locationText.setHorizontalAlignment(SwingConstants.HORIZONTAL);
            locationText.setFont(new Font("Arial", Font.PLAIN, 18));

            add(locationText);

            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            //Background image
            background = new JLabel(loadScaledImage("assets/background.jpg", 450, 700));
            background.setBounds(0, 0, 450, 700);
            add(background);

            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            //Search Action

            class SearchAction extends AbstractAction{
                @Override
                public void actionPerformed(ActionEvent e) {
                    //Get location from the user
                    String cityUserInput = citySearchField.getText();
                    String provinceUserInput = provinceSearchField.getText();
                    String countryUserInput = countrySearchField.getText();

                    //format input
                    if(cityUserInput.replaceAll("\\s", "").length() <= 0){
                        return;
                    }
                    provinceUserInput.replaceAll("\\s", "");
                    countryUserInput.replaceAll("\\s", "");

                    //retrieve the weather data
                    JSONObject weatherData = App.getWeatherData(cityUserInput, provinceUserInput, countryUserInput);
                    //update GUI
                    if(weatherData == null){
                        errorSound.play();
                        weatherConditionImage.setIcon(loadScaledImage("assets/error.png", 200, 200));
                        temperatureText.setText("");
                        weatherConditionText.setText("Please enter a valid city");
                        humidityText.setText("");
                        windSpeedText.setText("");
                        locationText.setText("");
                        displayingCity = false;
                        return;
                    }
                    displayingCity = true;

                    //update image
                    String weatherCondition = (String) weatherData.get("weather_condition");

                    //changing the image accordingly
                    switch(weatherCondition){
                        case "Cloudy":
                            weatherConditionImage.setIcon(loadImage("assets/cloudy.png"));
                            break;
                        case "Clear":
                            weatherConditionImage.setIcon(loadImage("assets/clear.png"));
                            break;
                        case "Rain":
                            weatherConditionImage.setIcon(loadImage("assets/rain.png"));
                            break;
                        case "Snow":
                            weatherConditionImage.setIcon(loadImage("assets/snow.png"));
                            break;
                    }

                    //Update temperature text
                    temperature = (double) weatherData.get("temperature");
                    if(inFahrenheit){
                        temperature = (int) (temperature * (9.0 / 5.0) + 32);
                        temperatureText.setText(temperature + " F");
                    }
                    else{
                        temperatureText.setText(temperature + " C");
                    }


                    //Update weather condition text
                    weatherConditionText.setText(weatherCondition);

                    //Update humidity text
                    long humidity = (long) weatherData.get("humidity");
                    humidityText.setText("<html><b>Humidity</b><br> " + humidity + "%<html>");

                    //Update wind speed text
                    double windSpeed = (double) weatherData.get("wind_speed");
                    windSpeedText.setText("<html><b>Wind Speed</b><br> " + windSpeed + "Km/h<html>");

                    //Update location text
                    String cityName = (String) weatherData.get("city");
                    String provinceName = (String) weatherData.get("province");
                    String countryName = (String) weatherData.get("country");

                    locationText.setText(cityName + ", " + provinceName + " " + countryName);

                }
            }

            Action searchAction = new SearchAction();

            //Giving action to appropriate components
            searchButton.addActionListener(searchAction);
            citySearchField.addActionListener(searchAction);
            provinceSearchField.addActionListener(searchAction);
            countrySearchField.addActionListener(searchAction);

            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            //Settings Action
            settingsButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    cl.next(cardPanel);
                }
            });

        }
        public JLabel getWeatherConditionText(){
            return weatherConditionText;
        }
        public void setToFahrenheit(Boolean switchToFahrenheit){
            if (switchToFahrenheit) {
                inFahrenheit = true;
                temperature = (int) (temperature * (9.0 / 5.0) + 32);
                if(displayingCity){
                    temperatureText.setText(temperature + " F");
                }
            } else {
                inFahrenheit = false;
                temperature = (int) ((temperature - 32) * (5.0 / 9.0));
                if(displayingCity){
                    temperatureText.setText(temperature + " C");
                }
            }
        }

        public void muteAll(){
            music.mute();
            errorSound.mute();
        }
    }
    class SettingsPanel extends JPanel{
        JButton settingsButton;
        JLabel background;
        JLabel settingsText;
        JLabel volumeMuteText;
        JLabel unitTitleText;
        JLabel unitText;
        JLabel audioText;
        CustomJCheckBox unitCheckBox;
        CustomJCheckBox muteCheckBox;
        Color boxSelectedColor = new Color(133, 212, 255);
        Color boxNonSelectedColor = new Color(211, 222, 255);

        SettingsPanel(){
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            //Settings Button
            settingsButton = new JButton(loadScaledImage("assets/settings.png", 25, 25));

            settingsButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            settingsButton.setBounds(400, 0, 30, 30);
            settingsButton.setContentAreaFilled(false);
            settingsButton.setBorderPainted(false);

            add(settingsButton);

            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            //Settings Text
            settingsText = new JLabel("Settings");

            settingsText.setBounds(20,20, 300,50);
            settingsText.setFont(new Font("Arial", Font.BOLD, 32));

            add(settingsText);

            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            //Units Text
            unitTitleText = new JLabel("Units");

            unitTitleText.setBounds(20,70, 300,50);
            unitTitleText.setFont(new Font("Arial", Font.BOLD, 24));

            add(unitTitleText);

            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            //Set to fahrenheit Text
            unitText = new JLabel("Set to Fahrenheit");

            unitText.setBounds(20,100, 150,50);
            unitText.setFont(new Font("Arial", Font.PLAIN, 18));

            add(unitText);

            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            //Units CheckBox
            unitCheckBox = new CustomJCheckBox(boxNonSelectedColor, boxSelectedColor);

            unitCheckBox.setBounds(175, 115, 20, 20);

            add(unitCheckBox);

            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            //Audio Title Text
            audioText = new JLabel("Audio");

            audioText.setBounds(20,150, 300,50);
            audioText.setFont(new Font("Arial", Font.BOLD, 24));

            add(audioText);

            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            //volume mute Text
            volumeMuteText = new JLabel("Mute volume");

            volumeMuteText.setBounds(20,180, 150,50);
            volumeMuteText.setFont(new Font("Arial", Font.PLAIN, 18));

            add(volumeMuteText);

            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            //Mute CheckBox
            muteCheckBox = new CustomJCheckBox(boxNonSelectedColor, boxSelectedColor);

            muteCheckBox.setBounds(175, 195, 20, 20);

            add(muteCheckBox);

            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            //Background image
            background = new JLabel(loadScaledImage("assets/background.jpg", 450, 700));
            background.setBounds(0, 0, 450, 700);
            add(background);

            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            //Settings Action
            settingsButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    cl.next(cardPanel);
                }
            });

            unitCheckBox.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    homePanel.setToFahrenheit(unitCheckBox.isSelected());
                }
            });

            muteCheckBox.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    homePanel.muteAll();
                }
            });
        }
    }

    private ImageIcon loadImage(String path){
        try{
            BufferedImage image = ImageIO.read(getClass().getResourceAsStream(path));
            return new ImageIcon(image);
        }
        catch(IOException e){
            e.printStackTrace();
        }

        System.out.println("Image Resource was not found!!!");
        return null;
    }
    private ImageIcon loadScaledImage(String path, int width, int height){
        Image img =  loadImage(path).getImage();
        Image newImage = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);

        return new ImageIcon(newImage);
    }
}
