package core;

import javax.swing.*;

public class Helper {
    public static void setTheme() {
        for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
            if (info.getName().equals("Nimbus")) {
                try {
                    UIManager.setLookAndFeel(info.getClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
                         UnsupportedLookAndFeelException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    public static boolean isFieldEmpty(JTextField field) {
        return field.getText().trim().isEmpty();
    }

    public static boolean isFieldListEmpty(JTextField[] fields) {
        for (JTextField field : fields) {
            if (isFieldEmpty(field)) return true;
        }
        return false;
    }

    public static boolean isEmailValid(String mail) {
        if (mail == null || mail.trim().isEmpty()) return false;

        if (!mail.contains("@")) return false;

        String[] parts = mail.split("@");
        if (parts.length != 2) return false;

        if (parts[0].trim().isEmpty() || parts[1].trim().isEmpty()) return false;

        if (!parts[1].contains(".")) return false;

        return true;
    }

//    public static void optionPaneDialogTR() {
//        UIManager.put("OptionPane.okButtonText", "Tamam");
//    }

    public static void showMessage(String message) {
//        optionPaneDialogTR();
        String msg;
        String title = switch (message) {
            case "fill" -> {
                msg = "Lütfen tüm alanları doldurunuz";
                yield "Hata!";
            }
            case "done" -> {
                msg = "İşlem Başarılı";
                yield "Sonuc";
            }
            case "error" -> {
                msg = "Bir hata oluştu";
                yield "Hata!";
            }
            default -> {
                msg = message;
                yield "Mesaj";
            }
        };

        JOptionPane.showMessageDialog(null, msg, title, JOptionPane.INFORMATION_MESSAGE);
    }
}
