import java.io.File;
import java.io.IOException;

import org.apache.commons.digester3.Digester;
import org.xml.sax.SAXException;

public class AddressBookParser
{
    /**
     * Prints the contact information to standard output.
     *
     * @param contact the <code>Contact</code> to print out
     */
    public void addContact(Contact contact)
    {
        System.out.println("TYPE: " + contact.getType());
        System.out.println("NAME: " + contact.getName());
        System.out.println("    ADDRESS:    " + contact.getAddress());
        System.out.println("    CITY:       " + contact.getCity());
        System.out.println("    PROVINCE:   " + contact.getProvince());
        System.out.println("    POSTALCODE: " + contact.getPostalcode());
        System.out.println("    COUNTRY:    " + contact.getCountry());
        System.out.println("    TELEPHONE:  " + contact.getTelephone());
    }

    /**
     * Configures Digester rules and actions, parses the XML file specified
     * as the first argument.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) throws IOException, SAXException
    {
        // instantiate Digester and disable XML validation
        Digester digester = new Digester();
        digester.setValidating(false);

        // instantiate AddressBookParser class
        digester.addObjectCreate("address-book", AddressBookParser.class );
        // instantiate Contact class
        digester.addObjectCreate("address-book/contact", Contact.class );

        // set type property of Contact instance when 'type' attribute is found
        digester.addSetProperties("address-book/contact",         "type", "type" );

        // set different properties of Contact instance using specified methods
        digester.addCallMethod("address-book/contact/name",       "setName", 0);
        digester.addCallMethod("address-book/contact/address",    "setAddress", 0);
        digester.addCallMethod("address-book/contact/city",       "setCity", 0);
        digester.addCallMethod("address-book/contact/province",   "setProvince", 0);
        digester.addCallMethod("address-book/contact/postalcode", "setPostalcode", 0);
        digester.addCallMethod("address-book/contact/country",    "setCountry", 0);
        digester.addCallMethod("address-book/contact/telephone",  "setTelephone", 0);

        // call 'addContact' method when the next 'address-book/contact' pattern is seen
        digester.addSetNext("address-book/contact",               "addContact" );

        // now that rules and actions are configured, start the parsing process
        AddressBookParser abp = (AddressBookParser) digester.parse("C:/Users/Suraj/Desktop/Digester1.xml.txt");
    }
}