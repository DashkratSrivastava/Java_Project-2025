import java.util.*;
import java.io.*;
import java.text.SimpleDateFormat;

public class TimeCapsule {
  private static final String FILE_NAME = "time_capsules.txt";
  private static Scanner scanner = new Scanner(System.in);

  public static void main(String[] args) {
    System.out.println(" Digital Time Capsule ");
    System.out.println("Save messages for your future self!");

    while (true) {
      System.out.println("\n1. Create Time Capsule");
      System.out.println("2. View Available Capsules");
      System.out.println("3. Exit");
      System.out.print("Choose: ");

      String choice = scanner.nextLine();

      switch (choice) {
        case "1":
          createCapsule();
          break;
        case "2":
          viewCapsules();
          break;
        case "3":
          System.out.println("Goodbye! ");
          return;
        default:
          System.out.println("Invalid choice!");
      }
    }
  }

  private static void createCapsule() {
    try {
      System.out.print("Enter your secret message: ");
      String message = scanner.nextLine();

      System.out.print("Open after how many days? ");
      int days = Integer.parseInt(scanner.nextLine());

      // Calculating future date
      Calendar calendar = Calendar.getInstance();
      calendar.add(Calendar.DAY_OF_YEAR, days);
      Date futureDate = calendar.getTime();

      // Simple "encryption" - just reversing for basic security
      String encryptedMessage = new StringBuilder(message).reverse().toString();

      // Save to file
      FileWriter writer = new FileWriter(FILE_NAME, true);
      writer.write(futureDate.getTime() + "|" + encryptedMessage + "\n");
      writer.close();

      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
      System.out.println("Capsule sealed! Can be opened after: " + sdf.format(futureDate));

    } catch (Exception e) {
      System.out.println("Error creating capsule: " + e.getMessage());
    }
  }

  private static void viewCapsules() {
    try {
      File file = new File(FILE_NAME);
      if (!file.exists()) {
        System.out.println("No time capsules found!");
        return;
      }

      BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME));
      String line;
      boolean foundOpenable = false;

      System.out.println("\n Your Time Capsules:");

      while ((line = reader.readLine()) != null) {
        String[] parts = line.split("\\|");
        if (parts.length == 2) {
          long timestamp = Long.parseLong(parts[0]);
          Date openDate = new Date(timestamp);
          Date now = new Date();

          if (now.after(openDate)) {
            // Decrypting the message (reverse back)
            String decryptedMessage = new StringBuilder(parts[1]).reverse().toString();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            System.out.println(" OPENED! Date: " + sdf.format(openDate));
            System.out.println("Message: " + decryptedMessage);
            foundOpenable = true;
          }
        }
      }
      reader.close();

      if (!foundOpenable) {
        System.out.println("No capsules ready to open yet. Be patient!");
      }

    } catch (Exception e) {
      System.out.println("Error reading capsules: " + e.getMessage());
    }
  }
}