import java.util.Objects;

public class Contact {
    String name,    surname;
    final String number;
    Group group;
    public Contact(String name, String surname, String number, Group group) {
        this.name = name;
        this.surname = surname;
        this.number = number;
        this.group = group;
    }

    public Contact(String newNumber, Contact oldContact) {
        name = oldContact.name;
        surname = oldContact.surname;
        number = newNumber;
        group = oldContact.group;
    }

    @Override
    public String toString() {
        return name + " " + surname + ": " + number + "(" + group + ")";
    }
    public String getShortString() {
        return name + " " + surname;
    }

    public Group getGroup() {
        return group;
    }
    public void setGroup(Group group) {
        this.group = group;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contact contact = (Contact) o;
        return Objects.equals(name, contact.name) &&
                Objects.equals(surname, contact.surname) &&
                Objects.equals(number, contact.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number);
    }

    public String getSurname() {
        return surname;
    }
    public void setSurname(String surname) {
        this.surname = surname;
    }
    public String getNumber() {
        return number;
    }

    enum Group {
        WORK("Работа"),
        FRIENDS("Друзья"),
        FAMILY("Семья");

        String representation;
        Group(String representation) {
            this.representation = representation;
        }
        @Override
        public String toString() {
            return representation;
        }
    }
}
