import java.util.Scanner;

// Doctor Class
class Doctor {
    int doctorId;
    String name;
    String specialization;
    String[] timeSlots;

    Doctor(int doctorId, String name, String specialization, String[] timeSlots) {
        this.doctorId = doctorId;
        this.name = name;
        this.specialization = specialization;
        this.timeSlots = timeSlots;
    }

    void display() {
        System.out.println(doctorId + ". " + name + " (" + specialization + ")");
    }
}

// Patient Class (Mobile Number used)
class Patient {
    String mobileNo;
    String name;
    int age;

    Patient(String mobileNo, String name, int age) {
        this.mobileNo = mobileNo;
        this.name = name;
        this.age = age;
    }
}

// Appointment Class
class Appointment {
    Doctor doctor;
    Patient patient;
    String timeSlot;
    String status;

    Appointment(Doctor doctor, Patient patient, String timeSlot) {
        this.doctor = doctor;
        this.patient = patient;
        this.timeSlot = timeSlot;
        this.status = "Booked";
    }

    void cancel() {
        status = "Cancelled";
    }

    void display() {
        System.out.println("\nDoctor: " + doctor.name + " (" + doctor.specialization + ")");
        System.out.println("Patient: " + patient.name);
        System.out.println("Mobile: " + patient.mobileNo);
        System.out.println("Time: " + timeSlot);
        System.out.println("Status: " + status);
    }
}

// Appointment Manager
class AppointmentManager {
    Appointment[] appointments = new Appointment[20];
    int count = 0;

    // Check availability
    boolean isSlotAvailable(int doctorId, String slot) {
        for (int i = 0; i < count; i++) {
            if (appointments[i].doctor.doctorId == doctorId &&
                appointments[i].timeSlot.equals(slot) &&
                appointments[i].status.equals("Booked")) {
                return false;
            }
        }
        return true;
    }

    // Show slots
    void showSlots(Doctor doctor) {
        System.out.println("\nAvailable Slots for " + doctor.name + ":");

        for (int i = 0; i < doctor.timeSlots.length; i++) {
            String slot = doctor.timeSlots[i];

            if (isSlotAvailable(doctor.doctorId, slot)) {
                System.out.println((i + 1) + ". " + slot);
            } else {
                System.out.println((i + 1) + ". " + slot + " (Booked)");
            }
        }
    }

    // Book appointment
    void bookAppointment(Doctor doctor, Patient patient, int slotChoice) {

        if (slotChoice < 1 || slotChoice > doctor.timeSlots.length) {
            System.out.println("❌ Invalid slot!");
            return;
        }

        String slot = doctor.timeSlots[slotChoice - 1];

        if (isSlotAvailable(doctor.doctorId, slot)) {
            appointments[count++] = new Appointment(doctor, patient, slot);
            System.out.println("✅ Appointment Booked with " + doctor.name + " at " + slot);
        } else {
            System.out.println("❌ Slot already booked!");
        }
    }

    // Cancel using mobile number
    void cancelAppointment(String mobileNo) {
        for (int i = 0; i < count; i++) {
            if (appointments[i].patient.mobileNo.equals(mobileNo) &&
                appointments[i].status.equals("Booked")) {

                appointments[i].cancel();
                System.out.println("✅ Appointment Cancelled");
                return;
            }
        }
        System.out.println("❌ Appointment not found");
    }

    // Display all
    void displayAppointments() {
        if (count == 0) {
            System.out.println("No appointments found.");
            return;
        }

        for (int i = 0; i < count; i++) {
            appointments[i].display();
        }
    }
}

// Main Class
public class HospitalSystem {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        // Doctors
        Doctor[] doctors = {
            new Doctor(101, "Dr. Sharma", "Cardiologist", new String[]{"10:00 AM", "11:00 AM"}),
            new Doctor(102, "Dr. Verma", "Dermatologist", new String[]{"12:00 PM", "1:00 PM"}),
            new Doctor(103, "Dr. Khan", "Neurologist", new String[]{"2:00 PM", "3:00 PM"}),
            new Doctor(104, "Dr. Gupta", "Orthopedic", new String[]{"4:00 PM", "5:00 PM"})
        };

        AppointmentManager manager = new AppointmentManager();

        while (true) {
            System.out.println("\n===== Hospital Appointment System =====");
            System.out.println("1. Book Appointment");
            System.out.println("2. Cancel Appointment");
            System.out.println("3. Show Appointments");
            System.out.println("4. Exit");
            System.out.print("Enter choice: ");

            int choice = sc.nextInt();

            switch (choice) {

                case 1:
                    System.out.println("\nSelect Doctor:");
                    for (Doctor d : doctors) {
                        d.display();
                    }

                    System.out.print("Enter Doctor ID: ");
                    int docId = sc.nextInt();

                    Doctor selectedDoctor = null;
                    for (Doctor d : doctors) {
                        if (d.doctorId == docId) {
                            selectedDoctor = d;
                        }
                    }

                    if (selectedDoctor == null) {
                        System.out.println("❌ Invalid Doctor!");
                        break;
                    }

                    sc.nextLine();

                    System.out.print("Enter Mobile Number: ");
                    String mobile = sc.nextLine();

                    System.out.print("Enter Patient Name: ");
                    String pname = sc.nextLine();

                    System.out.print("Enter Age: ");
                    int age = sc.nextInt();

                    manager.showSlots(selectedDoctor);

                    System.out.print("Select Slot: ");
                    int slotChoice = sc.nextInt();

                    Patient p = new Patient(mobile, pname, age);
                    manager.bookAppointment(selectedDoctor, p, slotChoice);
                    break;

                case 2:
                    System.out.print("Enter Patient Mobile Number: ");
                    String cancelMobile = sc.next();

                    manager.cancelAppointment(cancelMobile);
                    break;

                case 3:
                    manager.displayAppointments();
                    break;

                case 4:
                    System.out.println("Thank you!");
                    sc.close();
                    return;

                default:
                    System.out.println("Invalid choice!");
            }
        }
    }
}