import java.util.Scanner;

public class Main {
    static CallList callList;
    static ContactBase contactBase;
    static final long MAX_CALL_INTERVAL = 90_000_000L;
    static Scanner input = new Scanner(System.in);

    static {
        callList = new CallList();
        contactBase = ContactBase.getBaseExample();
        generateNewMissedCall("+7931-7463860");
        generateNewMissedCall("+7931-7463860");
        generateNewMissedCall("+7931-7422816");
    }

    public static void main(String[] args) {
        System.out.println("Телефонная книга с функцией пропущенных звонков!");
        boolean exit = false;
        while (!exit) {
            System.out.println("""

                    1 = добавить контакт
                    \t1+ = редактировать контакт
                    \t1- = удалить контакт
                    2 = имитация пропущенного звонка
                    3 = список пропущенных звонков
                    4 = опорожнение списка вызовов
                    0 = завершение программы""");
            switch (input.nextLine()) {
                case "0", "выход" -> exit = true;
                case "1", "добавить" -> System.out.println(addContact() ? "Контакт добавлен" : "Добавление отменено");
                case "1+", "редактировать" -> System.out.println(editContact() ? "Контакт изменён" : "Изменение отменено");
                case "1-", "удалить" -> System.out.println(deleteContact() ? "Контакт удалён" : "Удаление отменено");
                case "2", "звонок" -> generateNewMissedCall();
                case "3", "список" -> showMissedCallsList();
                case "4", "очистить" -> clear();
            }
        }
        System.out.println("Завшение работы с телефоном.");
    }

    private static boolean addContact() {
        System.out.print("Добавление контакта.");
        String name = getInput("Имя");
        if (name.equals("")) {
            System.out.println("Пустое поле, отмена добавления");
            return false;
        }
        String surname = getInput("Фамилия");
        if (surname.equals("")) {
            System.out.println("Пустое поле, отмена добавления");
            return false;
        }
        String number = getInput("Телефон");
        if (number.equals("")) {
            System.out.println("Пустое поле, отмена добавления");
            return false;
        }
        if (contactBase.containsNumber(number)) {
            System.out.println("Контакт с таким номером уже присутствует. Заменить? (+ для подтверждения)");
            if (!input.nextLine().equals("+")) return false;
        }
        Contact.Group group = chooseGroup();
        contactBase.addContact(new Contact(name, surname, number, group));
        return true;
    }
    private static String getInput(String field) {
        System.out.print(field + ": ");
        return input.nextLine();
    }

    private static Contact.Group chooseGroup() {
        while (true) {
            System.out.println("Выберите группу контакта:\n1 = Работа\n2 = Друзья\n3 = Семья");
            switch (input.nextLine()) {
                case "1" : return Contact.Group.WORK;
                case "2" : return Contact.Group.FRIENDS;
                case "3" : return Contact.Group.FAMILY;
            }
        }
    }

    private static boolean editContact() {
        boolean modified = false;
        Contact beingEdited = findContact("для редактирования");
        if (beingEdited == null) {
            System.out.println("Контакт не найден");
            return modified;
        }
        System.out.println("Имя = " + beingEdited.getName() +
                "\nВведите новое имя (или пустую строку оставить прежнее)");
        String newName = input.nextLine();
        if (!newName.equals("")) {
            beingEdited.setName(newName);
            modified = true;
        }
        System.out.println("Фамилия = " + beingEdited.getSurname() +
                "\nВведите новую фамилию (или пустую строку оставить прежнюю)");
        String newSurname = input.nextLine();
        if (!newSurname.equals("")) {
            beingEdited.setSurname(newSurname);
            modified = true;
        }
        System.out.println("Телефон = " + beingEdited.getNumber() +
                "\nВведите новый номер (или пустую строку оставить прежний)");
        String newNumber = input.nextLine();
        if (!newNumber.equals("")) {
            Contact changed = new Contact(newNumber, beingEdited);
            contactBase.addContact(changed);
            contactBase.removeContact(beingEdited.getNumber());
            beingEdited = changed;                              // ссылка на удалённый не теряется! :)
            modified = true;
        }
        System.out.println("Группа = " + beingEdited.getGroup());
        Contact.Group newGroup = chooseGroup();
        if (newGroup != beingEdited.getGroup()) {
            beingEdited.setGroup(newGroup);
            modified = true;
        }
        return modified;
    }

    private static Contact findContact(String purpose) {
        System.out.println("Выбрать контакт " + purpose + " по:\n" +
                "1 = имени и фамилии\n2 = номеру телефона");
        while (true) {
            switch (input.nextLine()) {
                case "1" -> {
                    System.out.println("Введите имя:");
                    String name = input.nextLine();
                    System.out.println("Введите фамилию:");
                    String surname = input.nextLine();
                    return contactBase.getContactByNameSurname(name, surname);
                }
                case "2" -> {
                    System.out.println("Введите номер:");
                    return contactBase.getContactByNumber(input.nextLine());
                }
            }
        }
    }

    private static boolean deleteContact() {
        Contact beingDeleted = findContact("для удаления");
        if (beingDeleted == null) {
            System.out.println("Контакт не найден");
            return false;
        }
        contactBase.removeContact(beingDeleted.getNumber());
        return true;
    }

    static private void generateNewMissedCall(String number) {
        callList.promoteVirtualTime((long) (Math.random() * MAX_CALL_INTERVAL));
        callList.takeMissedCall(number);
    }
    static private void generateNewMissedCall() {
        System.out.println("Введите номер звонка:");
        String number = input.nextLine();
        if (!number.equals(""))
            generateNewMissedCall(number);
    }

    static void showMissedCallsList() {
        System.out.println("Пропущенные звонки:");
        String[] lines = callList.giveMissedCalls(contactBase);
        for (String line : lines)
            System.out.println(line);
        if (lines.length == 0)
            System.out.println("<список пуст>");
    }

    static void clear() {
        System.out.println("Очистить список звонков? (+ для подтверждения)");
        if (input.nextLine().equals("+")) {
            callList.clear();
            System.out.println("Список пропущенных звонков опустошён.");
        } else {
            System.out.println("Очистка отменена");
        }
    }

}
