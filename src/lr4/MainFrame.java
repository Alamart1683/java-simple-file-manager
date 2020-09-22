package lr4;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.Date;
import javax.swing.*;
import java.util.regex.*;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.*;
import java.nio.charset.*;
import java.util.*;

// Главная форма программы
public class MainFrame extends javax.swing.JFrame {
    // Переменная лога
    static final File log = new File("log.txt");
    // Переменная директории
    static final String dir = "c:\\os4";
    // Переменная резервного хранилища
    static final String backup_dir = "c:\\ProgramData\\os4_backup";
    // Создание модели списка
    static DefaultListModel list_model = new DefaultListModel();

    public MainFrame() {
        initComponents();
        setLocationRelativeTo(null);
        jList1.setModel(list_model);
        // Переопределение двойного клика по элементу списка
        jList1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                MainFrame.restoratio_log();
                current_integrity();
                if (event.getClickCount() == 2) {
                    try {
                        current_integrity();
                        String file_name = JOptionPane.showInputDialog(null, "Введите новое имя файла:", "Меню переименования файла", 3);
                        // Проверка на некорректные символы в имени файла
                        if (MainFrame.file_name_control(file_name)) {
                            JOptionPane.showMessageDialog(null, "Введено некорректное имя файла", "Ошибка ввода",JOptionPane.ERROR_MESSAGE);
                            MainFrame.log_update(log, "Exeption: An incorrect file name was entered: " + file_name);
                        }
                        // Проверка на зарезервированные имена в имени файла
                        else if (MainFrame.file_name_reserved(file_name)) {
                            JOptionPane.showMessageDialog(null, "Введено зарезервированное имя файла", "Ошибка ввода",JOptionPane.ERROR_MESSAGE);
                            MainFrame.log_update(log, "Exeption: An system-required file name was entered: " + file_name);
                        }
                        // Проверка на занятость имени файла
                        else if (MainFrame.file_name_free(file_name)) {
                            JOptionPane.showMessageDialog(null, "Введено зарезервированное имя файла", "Ошибка ввода",JOptionPane.ERROR_MESSAGE);
                            MainFrame.log_update(log, "Exeption: An existing file name has been entered: " + file_name);
                        }
                        // Переименование файла
                        else {
                            MainFrame.file_renaming(jList1.getSelectedValue(), file_name);
                        }
                    }
                    catch (NullPointerException ex) { }
                }
                // Вывод лога по нажатию пкм
                else if (event.getButton() == MouseEvent.BUTTON3) {
                    try { Process process = Runtime.getRuntime().exec("cmd /c notepad.exe " + log.getAbsolutePath()); }
                    catch (IOException e) { }
                }
            }
        });

        // Локализация кнопок jOptionPane
        UIManager.put("OptionPane.yesButtonText"   , "Да");
        UIManager.put("OptionPane.noButtonText"    , "Нет");
        UIManager.put("OptionPane.cancelButtonText", "Отмена");
        UIManager.put("OptionPane.okButtonText"    , "Ок");
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();
        jScrollPane2 = new javax.swing.JScrollPane();
        jEditorPane1 = new javax.swing.JEditorPane();
        jLabel3 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Файловая система");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jList1.setBorder(new javax.swing.border.MatteBorder(null));
        jList1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        jScrollPane1.setViewportView(jList1);
        jEditorPane1.setEditable(false);
        jScrollPane2.setViewportView(jEditorPane1);
        jLabel3.setText("Содержимое файла:");
        jButton2.setText("Сохранить изменения");
        jButton2.setActionCommand("");

        jButton1.setText("Создать файл");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Удалить выделенный файл");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText("Открыть выделенный файл");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 442, Short.MAX_VALUE)
                                        .addComponent(jScrollPane2)
                                        .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 315, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jLabel3)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(0, 0, Short.MAX_VALUE))
                                        .addComponent(jScrollPane1))
                                .addContainerGap())
        );
        pack();
    }

    // Обработка события закрытия формы
    private void formWindowClosing(java.awt.event.WindowEvent evt) {
        // Сообщить о конце сессии
        Date end_date = new Date();
        MainFrame.log_update(log, "Session end: " + end_date.toString());
        MainFrame.log_update(log, "");
        current_integrity();
    }

    // Обработка события нажатия на кнопку создания файла
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            String file_name = JOptionPane.showInputDialog(null, "Введите имя файла:", "Меню создания файла", 3);
            // Проверка на некорректные символы в имени файла
            if (MainFrame.file_name_control(file_name)) {
                JOptionPane.showMessageDialog(null, "Введено некорректное имя файла", "Ошибка ввода",JOptionPane.ERROR_MESSAGE);
                MainFrame.log_update(log, "Exeption: An incorrect file name was entered: " + file_name);
            }
            // Проверка на зарезервированные имена в имени файла
            else if (MainFrame.file_name_reserved(file_name)) {
                JOptionPane.showMessageDialog(null, "Введено зарезервированное имя файла", "Ошибка ввода",JOptionPane.ERROR_MESSAGE);
                MainFrame.log_update(log, "Exeption: An system-required file name was entered: " + file_name);
            }
            // Проверка на занятость имени файла
            else if (MainFrame.file_name_free(file_name)) {
                JOptionPane.showMessageDialog(null, "Введено зарезервированное имя файла", "Ошибка ввода",JOptionPane.ERROR_MESSAGE);
                MainFrame.log_update(log, "Exeption: An existing file name has been entered: " + file_name);
            }
            // Создание файла
            else {
                MainFrame.file_creating(file_name, false);
            }
        }
        catch (NullPointerException e) { }
    }

    // Обработка события нажатия на кнопку сохранений изменений файла
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {
        file_editing();
    }

    // Обработка события нажатия на кнопку удаления файла
    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {
        if (jList1.getSelectedValue() != null) {
            int dialogButton = JOptionPane.YES_NO_OPTION;
            int result = JOptionPane.showConfirmDialog (null,"Вы точно уверены?","Предупреждение",dialogButton);
            if (result == JOptionPane.YES_OPTION) {
                String file_name = jList1.getSelectedValue();
                file_deleting(file_name, false);
            }
        }
    }

    // Обработка события нажатия на кнопку открытия файла
    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {
        if (jList1.getSelectedValue() != null) {
            file_opening();
        }
    }

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        // Запуск главной формы
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                // Переменная даты и времени
                Date start_date = new Date();
                new MainFrame().setVisible(true);
                File cat = new File(dir);
                File back_cat = new File (backup_dir);
                // Создать файл лога если он не существует
                try {
                    if (!log.exists() && !Files.exists(Paths.get(backup_dir + "\\backup_log.txt"))) {
                        log.createNewFile();
                    }
                } catch (IOException e) {}
                // Восстановить лог из резервной копии, если он удален и копия существует
                File back_log = new File(backup_dir + "\\backup_log.txt");
                if (!log.exists() && Files.exists(Paths.get(backup_dir + "\\backup_log.txt"))) {
                    MainFrame.restoratio_log();
                }
                else if (log.length() != back_log.length()) {
                    restoratio_log();
                }
                // Первый запуск программы
                if (!cat.exists() && !back_cat.exists()) {
                    cat.mkdir();
                    MainFrame.log_update(log, "Catalog \"os4\" was not exist and was created");
                    JOptionPane.showMessageDialog(null, "Директория хранения файлов системы не существовала и была создана", "Уведомление", JOptionPane.WARNING_MESSAGE);
                }
                else if (!cat.exists() && back_cat.exists()) {
                    cat.mkdir();
                    restoratio_directorum();
                    MainFrame.log_update(log, "Catalog \"os4\" was restored after the removal from the outside");
                    JOptionPane.showMessageDialog(null, "Директория хранения файлов была восстановлена после удаления извне", "Уведомление", JOptionPane.WARNING_MESSAGE);
                }
                current_integrity();
                List<String> content = new ArrayList<>();
                // Создать запись в логе о начале сессии
                try { Files.lines(Paths.get(log.getPath()), StandardCharsets.UTF_8).forEach(content::add);
                } catch(IOException e) { }
                // Если лог пуст то внести данные о начале первой сессии
                if (content.isEmpty()) {
                    MainFrame.log_update(log, "Session start: " + start_date.toString());
                }
                int content_length = content.size();
                if (content_length > 3) {
                    String crush_detector = content.get(content_length - 1) + " " + content.get(content_length - 2) + " " + content.get(content_length - 3);
                    if (!crush_detector.contains("crushed")) {
                        MainFrame.log_update(log, "Session start: " + start_date.toString());
                    }
                    else {
                        MainFrame.log_recovery(log);
                    }
                }
                else {
                    MainFrame.log_update(log, "Session start: " + start_date.toString());
                }
                // Загрузить список файлов директории
                list_filling(dir);
            }
        });
    }
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JEditorPane jEditorPane1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JList<String> jList1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;

    // Метод обновления лога
    public static void log_update(File log, String message) {
        List<String> content = new ArrayList<>();
        try { Files.lines(Paths.get(log.getPath()), StandardCharsets.UTF_8).forEach(content::add);
        } catch(IOException e) { }
        try (FileWriter writer = new FileWriter(log, true)) {
            if (content.size() > 0) {
                writer.write(message + "\n");
            }
            else {
                writer.append(message + "\n");
            }
            writer.flush();
            writer.close();
        } catch(IOException ex) { }
        if (message.contains("Session start")) {
            MainFrame.directory_backuping(false);
        }
        else if (message.contains("Log was restored after") || message.contains("Log was recreated")) {
            if (content.get(content.size() - 1).equals("")) {
                directory_backuping(false);
            }
            else {
                directory_backuping(false);
            }
        }
        else {
            directory_backuping(true);
        }
    }

    // Метод заполнения списка именами файлов из каталога
    public static void list_filling(String dir) {
        list_model.clear();
        // Получение имени файла
        File cat = new File(dir);
        String file_names[] = cat.list();
        // Заполнение списка имен файлов
        for (String file_name : file_names) {
            File file = new File(dir + "\\" + file_name);
            list_model.addElement(file_name);
        }
    }

    // Метод создания файла в директории
    public static void file_creating(String file_name, boolean flag) {
        File file = new File(dir + "\\" + file_name);
        MainFrame.restoratio_log();
        current_integrity();
        try {
            if (!flag) {
                MainFrame.log_update(log, "Beginning creating file " + file_name);
                int dialogButton = JOptionPane.YES_NO_OPTION;
                int result = JOptionPane.showConfirmDialog (null,"Спровоцировать сбой в работе программы?","Вопрос", dialogButton);
                if (result == JOptionPane.YES_OPTION) {
                    MainFrame.log_update(log, "Program was crushed");
                    System.exit(-1);
                }
            }
            //try { Thread.currentThread().join(5000); } catch (InterruptedException ie) { }
            file.createNewFile();
            MainFrame.directory_backuping(true);
            MainFrame.list_filling(dir);
            MainFrame.log_update(log, "New file " + file_name + " was successfully created");
        }
        catch(IOException e) { }
    }

    // Метод переименования выбранного файла
    public static void file_renaming(String old_name, String file_name) {
        MainFrame.restoratio_log();
        current_integrity();
        File backup_file = new File(backup_dir + "\\backup_cat\\" + old_name);
        File old_file = new File(dir  + "\\" + old_name);
        File new_file = new File(dir + "\\" + file_name);
        old_file.renameTo(new_file);
        directory_backuping(true);
        try { Files.delete(Paths.get(backup_file.getAbsolutePath())); } catch(IOException e) { }
        list_filling(dir);
        MainFrame.log_update(log, "File " + old_name + " was successfully renamed to " + file_name) ;
    }

    // Метод удаления файла из директории
    public static void file_deleting(String file_name, boolean flag) {
        if (!list_model.isEmpty() || flag) {
            MainFrame.restoratio_log();
            current_integrity();
            File file = new File(dir + "\\" + file_name);
            File backup_file = new File(backup_dir + "\\backup_cat\\" + file_name);
            if (!flag) {
                MainFrame.log_update(log, "Beginning deleting file " + file_name);
                int dialogButton = JOptionPane.YES_NO_OPTION;
                int result = JOptionPane.showConfirmDialog (null,"Спровоцировать сбой в работе программы?","Вопрос", dialogButton);
                if (result == JOptionPane.YES_OPTION) {
                    MainFrame.log_update(log, "Program was crushed");
                    System.exit(-1);
                }
            }
            //try { Thread.currentThread().join(5000); } catch (InterruptedException ie) { }
            try { Files.delete(file.toPath()); } catch(IOException e) { }
            try { Files.delete(Paths.get(backup_file.getAbsolutePath())); } catch(IOException e) { }
            MainFrame.list_filling(dir);
            MainFrame.log_update(log, "File " + file_name + " was successfully deleted");
        }
    }

    // Метод открытия выделенного файла
    public void file_opening() {
        String file_name = jList1.getSelectedValue();
        try {
            MainFrame.restoratio_log();
            current_integrity();
            jEditorPane1.setEditable(true);
            jButton1.setEnabled(false);
            jButton3.setEnabled(false);
            jButton4.setEnabled(false);
            jList1.setEnabled(false);
            RandomAccessFile file = new RandomAccessFile(new File(dir + "\\" + file_name),"r");
            byte[] array = new byte[(int)file.length()];
            file.readFully(array);
            String content = new String(array, "UTF-8");
            jEditorPane1.setText(content);
            MainFrame.log_update(log, "File " + file_name + " was successfully opened");
            file.close();
        }
        catch (IOException e) { }
    }

    // Метод сохранения изменений выбранного файла
    public void file_editing() {
        String file_name = jList1.getSelectedValue();
        try {
            MainFrame.restoratio_log();
            current_integrity();
            if (Files.exists(Paths.get(dir + "\\" + file_name))) {
                RandomAccessFile file = new RandomAccessFile(new File(dir + "\\" + file_name),"rw");
                file.setLength(0);
                String content = jEditorPane1.getText();
                file.write(content.getBytes());
                jButton1.setEnabled(true);
                jButton3.setEnabled(true);
                jButton4.setEnabled(true);
                jList1.setEnabled(true);
                jEditorPane1.setEditable(false);
                jEditorPane1.setText("");
                MainFrame.log_update(log, "File " + file_name + " was successfully saved");
                file.close();
            }
            else {
                jButton1.setEnabled(true);
                jButton3.setEnabled(true);
                jButton4.setEnabled(true);
                jList1.setEnabled(true);
                jEditorPane1.setEditable(false);
                jEditorPane1.setText("");
            }
        }
        catch (IOException e) { MainFrame.log_update(log, "File " + file_name + " failed to save");}
    }

    // Метод создания резервной копии директории
    public static void directory_backuping(boolean start_flag) {
        File bac = new File(backup_dir);
        File backup_cat = new File(backup_dir + "\\backup_cat");
        File backup_log = new File(backup_dir + "\\backup_log.txt");
        if (!bac.exists()) {
            bac.mkdirs();
            backup_cat.mkdirs();
        }
        try {
            List<String> lines = new ArrayList<>();
            Files.lines(Paths.get("log.txt"), StandardCharsets.UTF_8).forEach(lines::add);
            Files.write(backup_log.toPath(), lines);
            if (start_flag) {
                Path sourcepath = Paths.get(dir);
                Path destinationepath = Paths.get(backup_dir + "\\backup_cat");
                Files.walk(sourcepath).filter(Files::isRegularFile).forEach(source -> copy(source, destinationepath.resolve(sourcepath.relativize(source)), true));
            }
        }
        catch (IOException e) { }
    }

    // Метод копирования файла
    static void copy(Path source, Path dest, boolean flag) {
        try {
            if (flag) {
                Files.copy(source, dest, StandardCopyOption.REPLACE_EXISTING);
            }
            else {
                Files.copy(source, dest);
            }
        } catch (IOException e) {}
    }

    // Метод восстановления директории из резервной копии
    public static void restoratio_directorum() {
        try {
            delete_all();
            Path sourcepath = Paths.get(backup_dir + "\\backup_cat");
            Path destinationepath = Paths.get(dir);
            Files.walk(sourcepath).forEach(source -> copy(source, destinationepath.resolve(sourcepath.relativize(source)), false));
        } catch (IOException e) { }
    }

    // Метод зачистки директории
    public static void delete_all() {
        for (File my_file : new File(dir).listFiles())
            if (my_file.isFile()) {
                my_file.delete();
            }
            else if (my_file.isDirectory()) {
                my_file.delete();
            }
    }

    // Метод восстановления журнала из резервной копии
    public static void restoratio_log() {
        try {
            File backup_log = new File(backup_dir + "\\backup_log.txt");
            if (!log.exists()) {
                log.createNewFile();
                List<String> lines = new ArrayList<>();
                try {
                    Files.lines(backup_log.toPath(), StandardCharsets.UTF_8).forEach(lines::add);
                    Files.write(Paths.get("log.txt"), lines);
                } catch (IOException e) { }
                MainFrame.log_update(log, "Log was recreated after removing");
                JOptionPane.showMessageDialog(null, "Журнал был восстановлен после удаления извне", "Уведомление", JOptionPane.WARNING_MESSAGE);
            }
            else if (log.lastModified() > backup_log.lastModified()) {
                List<String> lines = new ArrayList<>();
                try {
                    Files.lines(backup_log.toPath(), StandardCharsets.UTF_8).forEach(lines::add);
                    Files.write(Paths.get("log.txt"), lines);
                } catch (IOException e) { }
                MainFrame.log_update(log, "Log was restored after outside changing");
                JOptionPane.showMessageDialog(null, "Журнал был восстановлен после изменения извне", "Уведомление", JOptionPane.WARNING_MESSAGE);
            }
        } catch (IOException ex) { }
    }

    // Метод проверки имени файла на корректность
    public static boolean file_name_control(String file_name) {
        if (file_name.equals("")) { return true; }
        Pattern pattern = Pattern.compile("(.+)?[><\\|\\?*/:\\\\\"](.+)?");
        Matcher matcher = pattern.matcher(file_name);
        return matcher.find();
    }

    // Метод проверки имени файла на занятость
    public static boolean file_name_free(String file_name) {
        File file = new File(dir + "\\" + file_name);
        if (file.exists()) { return true; }
        else { return false; }
    }

    // Метод проверки имени файла на зарезервированность
    public static boolean file_name_reserved(String file_name) {
        String reserved_names[] = {"CON", "NUL", "COM1", "COM2", "COM3",
                "LPT1", "LPT2", "LPT3", "AUX PRN", "COM4", "COM5", "COM6", "COM7",
                "COM8", "COM9", "LPT4", "LPT5", "LPT6", "LPT7", "LPT8", "LPT9"};
        for (String reserved_name : reserved_names) {
            if (file_name.contains(reserved_name)) { return true; }
        } return false;
    }

    // Метод обеспечения текущей целостности
    public static void current_integrity() {
        File catalog = new File(dir);
        if (!catalog.exists()) {
            catalog.mkdir();
            restoratio_directorum();
            MainFrame.log_update(log, "Catalog \"os4\" was restored after the removal from the outside");
            JOptionPane.showMessageDialog(null, "Директория хранения файлов была восстановлена после удаления извне", "Уведомление", JOptionPane.WARNING_MESSAGE);
        }
        File copy = new File(backup_dir + "\\backup_cat");
        File[] catalog_list = catalog.listFiles();
        File[] backup_list = copy.listFiles();
        if (backup_list.length > catalog_list.length) {
            restoratio_directorum();
            log_update(log, "The directory was successfully restored after adding or deleting files from the outside");
            JOptionPane.showMessageDialog(null, "Файлы системы восстановлены после добавления/удаления файлов извне", "Уведомление", JOptionPane.WARNING_MESSAGE);
            return;
        }
        else if (backup_list.length < catalog_list.length) {
            restoratio_directorum();
            log_update(log, "The directory was successfully restored after adding or deleting files from the outside");
            JOptionPane.showMessageDialog(null, "Файлы системы восстановлены после добавления/удаления файлов извне", "Уведомление", JOptionPane.WARNING_MESSAGE);
            return;
        }
        for (int index = 0; index < backup_list.length; index++) {
            if (!catalog_list[index].getName().equals(backup_list[index].getName())) {
                restoratio_directorum();
                log_update(log, "The directory was successfully restored after adding or deleting files from the outside");
                JOptionPane.showMessageDialog(null, "Файлы системы восстановлены после добавления/удаления файлов извне", "Уведомление", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }
        //String long_message = "";
        boolean flag = false;
        for (int index = 0; index < backup_list.length; index++) {
            long l1 = catalog_list[index].lastModified();
            long l2 = backup_list[index].lastModified();
            if (catalog_list[index].lastModified() != backup_list[index].lastModified()) {
                //long_message += "File " + catalog_list[index].getName() + " was changed from the outside and successfully restored\n";
                flag = true;
            }
        }
        if (flag) {
            //long_message = long_message.substring(0, long_message.length() - 1);
            restoratio_directorum();
            //log_update(log, long_message);
            log_update(log, "System's files was changed from the outside and successfully restored");
            JOptionPane.showMessageDialog(null, "Файлы системы восстановлены после вмешательства извне", "Уведомление", JOptionPane.WARNING_MESSAGE);
        }
    }

    // Метод восстановления лога в случае аварийного завершения работы во время создания/удаления
    public static void log_recovery(File log) {
        List<String> content = new ArrayList<>();
        try {
            Files.lines(Paths.get(log.getPath()), StandardCharsets.UTF_8).forEach(content::add);
            Date end_date = new Date();
            // Опеределение места сбоя в программе при удалении/созданиии
            if (content.get(content.size() - 2).contains("creating") || content.get(content.size() - 3).contains("creating")
                    || content.get(content.size() - 4).contains("creating")) {
                int dialogButton = JOptionPane.YES_NO_OPTION;
                int result = JOptionPane.showConfirmDialog (null,"Продолжить создание файла после сбоя программы?","Вопрос", dialogButton);
                if (result == JOptionPane.YES_OPTION) {
                    String[] first_buffer = content.get(content.size() - 2).split(" ");
                    String[] second_buffer = content.get(content.size() - 3).split(" ");
                    String[] third_buffer = content.get(content.size() - 4).split(" ");
                    MainFrame.log_update(log, "Program crash has been fixed");
                    // Восстановление процесса создания файла
                    if (content.get(content.size() - 2).contains("creating")) {
                        if (Files.notExists(Paths.get(dir + "\\" + first_buffer[3]))) {
                            MainFrame.file_creating(first_buffer[3], true);
                        }
                        else {
                            MainFrame.log_update(log, "New file " + first_buffer[3] + " was successfully created");
                        }
                        log_restore_helper(end_date);
                    }
                    else if (content.get(content.size() - 3).contains("creating")) {
                        if (Files.notExists(Paths.get(dir + "\\" + second_buffer[3]))) {
                            MainFrame.file_creating(second_buffer[3], true);
                        }
                        else {
                            MainFrame.log_update(log, "New file " + second_buffer[3] + " was successfully created");
                        }
                        log_restore_helper(end_date);
                    }
                    else if (content.get(content.size() - 4).contains("creating")) {
                        if (Files.notExists(Paths.get(dir + "\\" + third_buffer[3]))) {
                            MainFrame.file_creating(third_buffer[3], true);
                        }
                        else {
                            MainFrame.log_update(log, "New file " + third_buffer[3] + " was successfully created");
                        }
                        log_restore_helper(end_date);
                    }
                }
                else {
                    MainFrame.log_update(log, "Program crash has been fixed");
                    MainFrame.log_update(log, "File wasn't created at the request of the user");
                    log_restore_helper(end_date);
                }
            }
            else if (content.get(content.size() - 2).contains("deleting") || content.get(content.size() - 3).contains("deleting") ||
                    content.get(content.size() - 4).contains("deleting")) {
                int dialogButton = JOptionPane.YES_NO_OPTION;
                int result = JOptionPane.showConfirmDialog (null,"Продолжить удаление файла после сбоя программы?","Вопрос", dialogButton);
                if (result == JOptionPane.YES_OPTION) {
                    String[] first_buffer = content.get(content.size() - 2).split(" ");
                    String[] second_buffer = content.get(content.size() - 3).split(" ");
                    String[] third_buffer = content.get(content.size() - 4).split(" ");
                    MainFrame.log_update(log, "Program crash has been fixed");
                    // Восстановление процесса удаления файла
                    if (content.get(content.size() - 2).contains("deleting")) {
                        if (!Files.notExists(Paths.get(dir + "\\" + first_buffer[3]))) {
                            MainFrame.file_deleting(first_buffer[3], true);
                        }
                        else {
                            MainFrame.log_update(log, "File " + first_buffer[3] + " was successfully deleted");
                        }
                        log_restore_helper(end_date);
                    }
                    else if (content.get(content.size() - 3).contains("deleting")) {
                        if (!Files.notExists(Paths.get(dir + "\\" + second_buffer[3]))) {
                            MainFrame.file_deleting(second_buffer[3], true);
                        }
                        else {
                            MainFrame.log_update(log, "File " + second_buffer[3] + " was successfully deleted");
                        }
                        log_restore_helper(end_date);
                    }
                    else if (content.get(content.size() - 4).contains("deleting")) {
                        if (!Files.notExists(Paths.get(dir + "\\" + third_buffer[3]))) {
                            MainFrame.file_deleting(third_buffer[3], true);
                        }
                        else {
                            MainFrame.log_update(log, "File " + third_buffer[3] + " was successfully deleted");
                        }
                        log_restore_helper(end_date);
                    }
                }
                else {
                    MainFrame.log_update(log, "Program crash has been fixed");
                    MainFrame.log_update(log, "File wasn't deleted at the request of the user");
                    log_restore_helper(end_date);
                }
            }} catch (IOException ex) { } catch (ArrayIndexOutOfBoundsException ae) { }
    }

    // Метод блокирования файлов от ручного вмешательства во время работы программы
    public static void directory_protection() {
        try {
            File cat = new File(dir);
            String file_names[] = cat.list();
            List<FileReader> protection_read_list = new ArrayList<>();
            List<FileWriter> protection_write_list = new ArrayList<>();
            for (String file_name : file_names) {
                File file = new File(dir + "\\" + file_name);
                FileReader reader = new FileReader(file);
                FileWriter writer = new FileWriter(file);
                protection_read_list.add(reader);
                protection_write_list.add(writer);
            }} catch (Exception e) { }
    }

    // Метод восстановления целостности лога после сбоя в работе
    public static void log_restore_helper(Date end_date) {
        MainFrame.log_update(log, "Session end: " + end_date.toString());
        MainFrame.log_update(log, "");
        MainFrame.log_update(log, "Session start: " + end_date.toString());
    }
}
