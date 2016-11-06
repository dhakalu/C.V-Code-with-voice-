import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.LiveSpeechRecognizer;
import org.apache.http.client.utils.URIBuilder;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultEditorKit;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;



public class TextEditor extends JFrame {


    private String currentFile = "Untitled";

    /**
     * This is the variable that keeps track of weather the text was modified
     *
     */
    private boolean changed = false;

    private final Action About;
    private final Action New;
    private final Action Open;
    private final Action Save;
    private final Action SaveAs;

    private final Action Cut;
    private final Action Copy;
    private final Action Paste;
    private final Action CheckForUpdates;

    private final Action Record;


    private JTextArea editArea = new JTextArea(20,120);
    private JTextArea terminalArea;
    private JTextArea stackOverFlowArea;
    private JTextArea stackOverFlowInput;

    private JMenuBar menuBar;
    private JToolBar toolBar;

    private JMenu fileMenu;
    private JMenu editMenu;
    private JMenu helpMenu;

    private boolean isRecording;
    private JFileChooser fileChooser;

    private LiveSpeechRecognizer recognizer = null;


    private HashMap<String, String> mapOfCommands = new HashMap<>();

    public TextEditor(){


        terminalArea =  new JTextArea(20, 120);
        Border border = BorderFactory.createLineBorder(Color.DARK_GRAY, 20);
        terminalArea.setBorder(border);
        terminalArea.setMargin( new Insets(10,10,10,10) );
        //terminalArea.setCaretColor(new Color());


        terminalArea.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER){

                    try {
                        int offset= terminalArea.getLineOfOffset( terminalArea.getCaretPosition());
                        int start= terminalArea.getLineStartOffset(offset);
                        int end= terminalArea.getLineEndOffset(offset);

                        System.out.println("Text: "+ terminalArea.getText(start, (end-start)));
                        executeCommand(terminalArea.getText());

                    } catch (BadLocationException ex) {
                        System.out.println(ex.getMessage());
                    }
                }

            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

        editArea.setFont(new Font("Serif", Font.PLAIN, 20));
        JScrollPane scrollPane = new JScrollPane(editArea,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );
        scrollPane.setVisible(true);
        add(scrollPane);
        editArea.setBorder(border);
        editArea.setLineWrap(true);
        editArea.setWrapStyleWord(true);

        terminalArea.setMargin(new Insets(10, 10, 10, 10));


        JScrollPane scrollPaneForTerminal = new JScrollPane(terminalArea,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS
        );

        add(scrollPaneForTerminal);


        stackOverFlowArea = new JTextArea();
        stackOverFlowArea.setEditable(false);
        stackOverFlowArea.setBorder(border);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        stackOverFlowArea.setMaximumSize(new Dimension(screenSize.width/2 -100, screenSize.height/2 -100));
        // stackOverFlowArea.setLineWrap(true);
        stackOverFlowArea.setFont(new Font("Serif", Font.BOLD, 18));

        add(stackOverFlowArea);


        editArea.setSize(screenSize.width/2 -50, screenSize.height/2 -50 );

        JScrollPane scrollPaneForStackOverflow = new JScrollPane(stackOverFlowArea,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);



        fileMenu = new JMenu("File");
        fileMenu.setFont(new Font("Monospace", Font.PLAIN, 20));


        editMenu = new JMenu("Edit");
        editMenu.setFont(new Font("Monospace", Font.PLAIN, 20));


        helpMenu = new JMenu("Help");
        helpMenu.setFont(new Font("Monospace", Font.PLAIN, 20));


        menuBar = new JMenuBar();
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(helpMenu);
        menuBar.setBorderPainted(false);
        menuBar.setFont(new Font("Serif", Font.PLAIN, 20));


        fileChooser = new JFileChooser();

        Record = new AbstractAction("Record") {
            @Override
            public void actionPerformed(ActionEvent e) {
                startRecording();
            }
        };

        New = new AbstractAction("New") {
            @Override
            public void actionPerformed(ActionEvent e) {
                new TextEditor();
            }
        };

        Open = new AbstractAction("Open") {
            @Override
            public void actionPerformed(ActionEvent e) {
                openFile();
            }
        };



        Save = new AbstractAction("Save") {
            @Override
            public void actionPerformed(ActionEvent e) {
                previous();
            }
        };

        SaveAs = new AbstractAction("Save As") {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        };

        About = new AbstractAction("About") {
            @Override
            public void actionPerformed(ActionEvent e) {
                AboutPop aboutWin = new AboutPop();

            }
        };

        CheckForUpdates = new AbstractAction("Check For Updates") {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        };

        ActionMap map = editArea.getActionMap();
        Cut = map.get(DefaultEditorKit.cutAction);
        Copy = map.get(DefaultEditorKit.copyAction);
        Paste = map.get(DefaultEditorKit.pasteAction);


        fileMenu.add(New);
        fileMenu.add(Open);
        fileMenu.add(Save);
        fileMenu.add(SaveAs);

        editMenu.add(Cut);
        editMenu.add(Copy);
        editMenu.add(Paste);


        toolBar = new JToolBar("Sample Button");
        toolBar.add(Record);

        add(editArea);
        add(terminalArea, BorderLayout.SOUTH);
        add(stackOverFlowArea, BorderLayout.EAST);
        add(toolBar, BorderLayout.NORTH);
        setJMenuBar(menuBar);
        setTitle(currentFile);
        // setSize(500, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        // pack();
        setBounds(0,0,screenSize.width, screenSize.height);
        setVisible(true);

        createCommandMap();

    }

    private void createCommandMap() {

        mapOfCommands.put("create a class", "\n public class HelloWorld { \n }");
        mapOfCommands.put("create a method", "\n public void doSomething() {\n }");
        mapOfCommands.put("create main method", "\n public static void main(String[] args) {\n }");
    }


    private void openFile() {
        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
            readInFile(fileChooser.getSelectedFile().getAbsolutePath(), editArea);
    }

    private void readInFile(String absolutePath, JTextArea editArea) {
        try {
            FileReader fileReader = new FileReader(new File(absolutePath));
            editArea.read(fileReader, null);
            fileReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void startRecording(){
        terminalArea.append("Starting to record");
        Configuration configuration = new Configuration();
        configuration.setAcousticModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us");
        configuration.setDictionaryPath("file:0850.dic");
        configuration.setLanguageModelPath("file:0850.lm");
        try {
            recognizer = new LiveSpeechRecognizer(configuration);
        } catch (IOException e) {
            e.printStackTrace();
        }

        recognizer.startRecognition(true);
        new SpeechToTextWorker().execute();
    }



    private class SpeechToTextWorker extends SwingWorker<Void, Void> {

        @Override
        public Void doInBackground() {
            System.out.println("Recording in background");
            System.out.println("Say something. Say Stop Recording to stop!");
            while (true) {
                String result = recognizer.getResult().getHypothesis().toLowerCase();
                if (result.startsWith("stop")){
                    break;
                } else if (result.equals("create a method")){
                    System.out.println(result);
                    editArea.append("\n public void printHelloWorld() { \n }");
                }  else if (result.equals("create main method")){
                    System.out.println(result);
                    editArea.append("\n public static void main(String[] args) { \n }");
                } else if (result.equals("create a class")){
                    System.out.println(result);
                    editArea.insert("\n public class HelloWorld { \n }", editArea.getCaretPosition());
                } else if(result.startsWith("print")){
                    System.out.println(result);
                    result.replaceAll("print", "");
                    editArea.insert("\n System.out.println(\" " + result    + "  \")", editArea.getCaretPosition());
                } else if (result.equals("open")){
                    openFile();
                } else {
                    System.out.println(result);
                    editArea.append(result);
                }
            }
            return null;
        }

        @Override
        public void done() {
            //TODO: Update the GUI with the updated list.
            recognizer.stopRecognition();

        }
    };



    private void executeCommand(String command){

        if (command.startsWith("search")){
            command.replaceAll("search", "");
            getOverflowData(command);
        } else if (command.trim().equals("clear")) {
            terminalArea.setText("");
        } else {

            Process p;
            String s;

            try {
                p = Runtime.getRuntime().exec(command);

                BufferedReader bufferedReader = new BufferedReader(
                        new InputStreamReader(p.getInputStream())
                );
                while ((s = bufferedReader.readLine()) != null){
                    terminalArea.append("\n" + s);
                }
                terminalArea.append("\n");
                p.waitFor();
                p.destroy();
            } catch (IOException e) {
                terminalArea.append(e.getMessage());
            } catch (InterruptedException e) {
                terminalArea.append(e.getMessage());
            }

        }
    }



    private void forward() {
        int currPos = editArea.getCaretPosition();
        if (currPos != editArea.getDocument().getLength()) { editArea.setCaretPosition(currPos + 1); };
    }
    private void backward() {
        int currPos = editArea.getCaretPosition();
        if (currPos != 0) editArea.setCaretPosition(currPos - 1);
    }

    private void next() {
        int currPos = editArea.getCaretPosition();
        while ((editArea.getText().charAt(currPos)) != ' ') {
            if (currPos == editArea.getDocument().getLength()) {
                return;
            }
            currPos += 1;
        }
        editArea.setCaretPosition(currPos + 1);
    }
    private void previous() {
        int currPos = editArea.getCaretPosition();
        int spaceCount = 0;
        while (spaceCount <2) {
            if (currPos == 0) {
                editArea.setCaretPosition(0);
                return;
            }
            if ((editArea.getText().charAt(currPos - 1)) == ' ') {
                spaceCount += 1;
            }
            currPos -= 1;
        }
        editArea.setCaretPosition(currPos + 1);
    }



    public void getOverflowData(String query){
        resetStackOverflowResultArea();
        final String baseUrl = "https://api.stackexchange.com/2.2/search/advanced";
        URI uriBuilder = null;
        try {
            uriBuilder = new URIBuilder(baseUrl)
                    .addParameter("order", "desc")
                    .addParameter("sort", "activity")
                    .addParameter("site", "stackoverflow")
                    .addParameter("q", query)
                    .build();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        System.out.println(uriBuilder.toString());
        JSONObject overflowResponse = new HttpUtils().getJSON(uriBuilder.toString());
        JSONArray items = overflowResponse.getJSONArray("items");

        for (int i = 0; i < items.length(); i++) {
            JSONObject currentQuestion = items.getJSONObject(i);
            System.out.println(currentQuestion.getString("title"));
            stackOverFlowArea.append("\n" + currentQuestion.getString("title") + "\n");
        }
    }

    private void resetStackOverflowResultArea(){
        stackOverFlowArea.setText("");
    }


}
