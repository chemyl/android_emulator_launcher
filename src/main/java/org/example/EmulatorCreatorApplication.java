package org.example;

import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Log4j
public class EmulatorCreatorApplication extends JFrame {

    private JComboBox<String> bitDepthComboBox;
    private JComboBox<String> windowsComboBox;
    private JComboBox<String> phoneModelComboBox;
    private JComboBox<String> apiVersionComboBox;
    private JComboBox<String> googleServicesComboBox;
    private JTextField quantityTextField;
    private boolean launching;
    private JTextArea outputTextArea;

    public EmulatorCreatorApplication() {
        setTitle("Android Emulator Launcher");
        setSize(600, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel inputPanel = createInputPanel();
        JPanel outputPanel = createOutputPanel();

        add(inputPanel, BorderLayout.NORTH);
        add(outputPanel, BorderLayout.CENTER);
    }

    private JPanel createInputPanel() {
        JPanel panel = new JPanel(new GridLayout(8, 2));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel quantityLabel = new JLabel("Quantity:");
        JLabel apiVersionLabel = new JLabel("API level:");
        JLabel deviceNameLabel = new JLabel("Emulator Model:");
        JLabel googleServicesLabel = new JLabel("Google services:");
        JLabel windowsTypeLabel = new JLabel("Emulator screen:");
        JLabel bitDepthLabel = new JLabel("Bit Depth:");

        String[] deviceName = {"Galaxy Nexus", "Nexus 10", "Nexus 4", "Nexus 5X", "Nexus 6P", "Nexus One"};
        phoneModelComboBox = new JComboBox<>(deviceName);

        String[] windowsType = {"screen -active", "screen -off"};
        windowsComboBox = new JComboBox<>(windowsType);

        String[] bitDepth = {"x86", "x86_64"};
        bitDepthComboBox = new JComboBox<>(bitDepth);

        String[] apiVersionOptions = {"33", "32", "31", "30", "29", "28", "27", "26", "25", "24", "23", "22"};
        apiVersionComboBox = new JComboBox<>(apiVersionOptions);

        String[] googleServices = {"default", "google_apis", "google_apis_playstore"};
        googleServicesComboBox = new JComboBox<>(googleServices);

        quantityTextField = new JTextField();
        quantityTextField.setText("1");

        //Panel order
        panel.add(quantityLabel);
        panel.add(quantityTextField);

        panel.add(deviceNameLabel);
        panel.add(phoneModelComboBox);

        panel.add(apiVersionLabel);
        panel.add(apiVersionComboBox);

        panel.add(googleServicesLabel);
        panel.add(googleServicesComboBox);

        panel.add(bitDepthLabel);
        panel.add(bitDepthComboBox);

        panel.add(windowsTypeLabel);
        panel.add(windowsComboBox);

        JButton executeButton = new JButton("Run emulators");
        executeButton.addActionListener(e -> {
            executeCommand();
            executeButton.setEnabled(false);
        });

        JButton destroyButton = new JButton("Stop emulators");
        destroyButton.addActionListener(e -> {
            destroyEmulators();
            destroyButton.setEnabled(false);
        });

        JCheckBox launchCheckBox = new JCheckBox("Launch activation");
        launchCheckBox.addActionListener(e -> {
            JCheckBox source = (JCheckBox) e.getSource();
            launching = source.isSelected();
        });

        panel.add(executeButton);
        panel.add(destroyButton);
        panel.add(launchCheckBox);
        return panel;
    }

    private JPanel createOutputPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel outputLabel = new JLabel("");
        outputTextArea = new JTextArea();
        outputTextArea.setEditable(true);
        outputTextArea.setText(
                "                                           Set android-emulator capabilities \n" +
                        "                                           check launch activation and press Run emulators \n" +
                        "                                           Wait for output result...");
        panel.add(outputLabel, BorderLayout.NORTH);
        panel.add(new JScrollPane(outputTextArea), BorderLayout.CENTER);

        return panel;
    }

    //Engine

    private static final String AWDMANAGER_PATH = "C:/Users/{username}/AppData/Local/Android/Sdk" +
            "/tools/bin/avdmanager.bat";
    private static final String SDKMANAGER_PATH = "C:/Users/username/AppData/Local/Android/Sdk" +
            "/tools/bin/sdkmanager.bat";
    private static final String EMULATOR_PATH = "C:/Users/username/AppData/Local/Android/Sdk" +
            "/emulator/emulator.exe";

    private void executeCommand() {
        int numOfEmulators = Integer.parseInt(quantityTextField.getText());
        String bitDepth = Objects.requireNonNull(bitDepthComboBox.getSelectedItem()).toString();
        String windowsEnabled = Objects.requireNonNull(windowsComboBox.getSelectedItem()).toString();
        String apiVersion = Objects.requireNonNull(apiVersionComboBox.getSelectedItem()).toString();
        String deviceName = Objects.requireNonNull(phoneModelComboBox.getSelectedItem()).toString();
        String googleServices = Objects.requireNonNull(googleServicesComboBox.getSelectedItem()).toString();

        String deviceImage = "system-images;android-" +
                apiVersion + ";" +
                googleServices + ";" +
                bitDepth;

        List<String> emulatorAddresses = createEmulators(numOfEmulators, deviceName, bitDepth, deviceImage, googleServices, windowsEnabled);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Emulator address:\n");
        for (String address : emulatorAddresses) {
            stringBuilder.append(address).append(System.lineSeparator());
        }
        outputTextArea.setText(stringBuilder.toString());
    }

    private void destroyEmulators() {
        outputTextArea.setText("Emulators stopped");
        //TODO avdmanager delete avd --name {avdName} or adb emu kill
    }

    @SneakyThrows
    public List<String> createEmulators(int numOfEmulators, String deviceName, String abi, String deviceImage,
                                        String googleServices, String windowType) {
        List<String> emulatorAddresses = new ArrayList<>();
        if (!isDevicePackagesInstalled(deviceImage)) {
            downloadAndInstallDevicePackages(deviceImage);
        }

        for (int i = 0; i < numOfEmulators; i++) {
            String emulatorName = deviceName.replace(" ", "_") + "." + i;
            String port = Integer.toString(5554 + (2 * i));

            String createAVDCommand = AWDMANAGER_PATH + " create avd --name " + "\"" + emulatorName +
                    "\" --package " + "\"" + deviceImage +
                    "\" --device " + "\"" + deviceName +
                    "\" --tag " + "\"" + googleServices +
                    "\" --abi " + "\"" + abi +
                    "\" --force";

            String launchEmulatorCommand;
            switch (windowType) {
                case "screen -active":
                    launchEmulatorCommand = EMULATOR_PATH +
                            " -avd " + emulatorName +
                            " -scale 0.5" +
                            " -port " + port;
                    break;
                case "screen -off":
                default:
                    launchEmulatorCommand = EMULATOR_PATH +
                            " -avd " + emulatorName +
                            " -port " + port +
                            " -no-audio " +
                            "-no-boot-anim " +
                            "-no-window";
                    break;
            }
            executeBashCommand(createAVDCommand);
            if (launching) {
                executeBashCommand(launchEmulatorCommand);
            }
            emulatorAddresses.add("127.0.0.1:" + port);
        }
        return emulatorAddresses;
    }

    @SneakyThrows
    public boolean isDevicePackagesInstalled(String deviceImage) {
        String listCmd = SDKMANAGER_PATH + " --list";
        String output = executeBashCommand(listCmd);
        String extracted = null;

        int startIndex = output.indexOf("Installed packages:");
        int endIndex = output.indexOf("Available Packages:");

        if (startIndex != -1 && endIndex != -1) {
            extracted = output.substring(startIndex, endIndex);
            log.debug(extracted);
        } else {
            System.out.println("Packages not found in response");
        }
        return extracted != null && extracted.contains(deviceImage);
    }

    @SneakyThrows
    public void downloadAndInstallDevicePackages(String deviceImage) {
        String downloadCommand = SDKMANAGER_PATH + " --install " + deviceImage;
        System.out.println("New image => " + deviceImage + " downloading....");
        executeBashCommand(downloadCommand);
    }

    @SneakyThrows
    public String executeBashCommand(String command) {
        log.debug(command);
        ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", command);
        if (command.contains("emulator")) {
            processBuilder.redirectOutput(ProcessBuilder.Redirect.PIPE);
        }
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        StringBuilder output = new StringBuilder();
        String line;
        if (command.contains("emulator")) {
            process.waitFor(10, TimeUnit.SECONDS);
        } else {
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
            int exitCode = process.waitFor();
            System.out.println("Process exited with code: " + exitCode);
        }
        return output.toString();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new EmulatorCreatorApplication().setVisible(true));
    }
}