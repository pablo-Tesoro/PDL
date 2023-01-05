package Gestor;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import lex.AnalizadorLexico;
import sinSem.AnalizadorSintacticoSemantico;

public class App extends JFrame {
  public App() {
    // Crea un nuevo botón con el texto "Seleccionar archivo"
    JButton button = new JButton("Seleccionar archivo");
    button.setBounds(100,200,500,150);
    button.setFont(new Font("Arial", Font.BOLD, 30));
    button.setIcon(new ImageIcon("C:/Users/pteso/Documents/PracticaPDL/PDL/images/icono.png"));
    button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    button.setBackground(Color.WHITE);
    button.setBorder(BorderFactory.createLineBorder(Color.BLACK, 5));
    setLayout(null);

    // Añade una acción de escucha al botón
    button.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        // Crea un nuevo objeto JFileChooser
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Text files", "txt"));
        fileChooser.setDialogTitle("JavaScript-PDL");
        File downloadsFolder = new File(System.getProperty("user.home"), "Downloads");
        fileChooser.setCurrentDirectory(downloadsFolder);

        // Muestra el cuadro de diálogo para abrir archivo
        int result = fileChooser.showOpenDialog(null);

        // Si el usuario hace clic en el botón Abrir
        if (result == JFileChooser.APPROVE_OPTION) {
          // Obtiene el archivo seleccionado
          File selectedFile = fileChooser.getSelectedFile();
          GestorErrores gestorErrores = new GestorErrores();
          AnalizadorLexico anLex = new AnalizadorLexico(selectedFile.getPath(), gestorErrores);
          anLex.generarToken();
          anLex.escribirTokens();
          gestorErrores = anLex.getGestorErrores();
          AnalizadorSintacticoSemantico anSin = new AnalizadorSintacticoSemantico(anLex, gestorErrores);
          anSin.genParse();
          anSin.generarTS();
          gestorErrores = anSin.getGestorErrores();
          gestorErrores.imprimirErrores();
          new newWindow();
        }
      }
    });

    // Añade el botón a la ventana
    add(button);

    // Establece el tamaño y la posición de la ventana
    getContentPane().setBackground(Color.WHITE);
    setSize(700, 600);
    setLocationRelativeTo(null);
  }
  

class FileContentWindow extends JFrame {

  public FileContentWindow(String content) {
        
    JTextArea textArea = new JTextArea(content);
    add(new JScrollPane(textArea));

    setSize(400, 300);
    setVisible(true);
  }
}

class newWindow extends JFrame {

  public newWindow() {

    JButton gramatica = new JButton("Gramatica");
    gramatica.setBounds(300,100,300,100);
    gramatica.setFont(new Font("Arial", Font.BOLD, 15));
    gramatica.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    gramatica.setBackground(Color.WHITE);
    gramatica.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
    setLayout(null);
    add(gramatica);

    JButton tokens = new JButton("Tokens");
    tokens.setBounds(300,220,300,100);
    tokens.setFont(new Font("Arial", Font.BOLD, 15));
    tokens.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    tokens.setBackground(Color.WHITE);
    tokens.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
    setLayout(null);
    add(tokens);

    JButton parse = new JButton("Parse");
    parse.setBounds(300,340,300,100);
    parse.setFont(new Font("Arial", Font.BOLD, 15));
    parse.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    parse.setBackground(Color.WHITE);
    parse.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
    setLayout(null);
    add(parse);

    JButton tablaSimbolos = new JButton("Tabla Simbolos");
    tablaSimbolos.setBounds(300,460,300,100);
    tablaSimbolos.setFont(new Font("Arial", Font.BOLD, 15));
    tablaSimbolos.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    tablaSimbolos.setBackground(Color.WHITE);
    tablaSimbolos.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
    setLayout(null);
    add(tablaSimbolos);
    JButton errores = new JButton("Errores");
    errores.setBounds(300,580,300,100);
    errores.setFont(new Font("Arial", Font.BOLD, 15));
    errores.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    errores.setBackground(Color.WHITE);
    errores.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
    setLayout(null);
    add(errores);

    gramatica.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
          getContent("C:/Users/pteso/Documents/PracticaPDL/PDL/pruebas/gramatica.txt");
      }
    });
    tokens.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
          getContent("C:/Users/pteso/Documents/PracticaPDL/PDL/pruebas/tokens.txt");
      }
    });
    parse.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
          getContent("C:/Users/pteso/Documents/PracticaPDL/PDL/pruebas/parse.txt");
      }
    });
    tablaSimbolos.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
          getContent("C:/Users/pteso/Documents/PracticaPDL/PDL/pruebas/tablaSimbolos.txt");
      }
    });
    errores.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
          getContent("C:/Users/pteso/Documents/PracticaPDL/PDL/pruebas/errores.txt");
      }
    });

    setSize(970, 800);
    setVisible(true);
  }

  public void getContent(String path){
    StringBuilder content = new StringBuilder();
    try  {
      File tablaFile = new File(path);
      FileReader tabla = new FileReader(tablaFile);
      try (BufferedReader reader = new BufferedReader(tabla)) {
        String line;
        while ((line = reader.readLine()) != null) {
          content.append(line + "\n");
        }
      }
    } catch (IOException ex) {
        ex.printStackTrace();
      }
    new FileContentWindow(content.toString());
  }
}

  public static void main(String[] args) {
    // Muestra la ventana
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        new App().setVisible(true);
      }
    });
  }
}