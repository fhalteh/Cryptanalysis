public class decryptWithKey {
	private String key;
	private String cipher_text;

	// Constructor to initiate this class
	// takes the key and the cipher text as parameters
	// computes the decryption by calling the decrypt method
	// prints out the plain text
	public decryptWithKey(String key, String cipher_text) {
		this.key = key;
		this.cipher_text = cipher_text;

		String plain_text = decrypt(cipher_text, key);
		System.out.println("This is the plain Text: " + plain_text);

	}

	// decrypt method is used to decrypt a cipher text using a key (Vigenere
	// Cipher)
	// it returns a plain text in String format.
	private static String decrypt(String cipher_text, String key) {
		String plain_text = "";

		// for loop that goes through every character in the cipher
		// it gets this character, and the character at the index in the key
		// in order to perform the calculations and return the decrypted
		// character
		for (int i = 0; i < cipher_text.length(); i++) {

			char current_char = cipher_text.charAt(i); // current character
			// gets the numeric value of the character (ex. a = 0, b = 1, and so
			// on)
			int num_value_of_char = letterToNumber(current_char);
			// gets the index value of the key to know which character of the
			// key is used here to decrypt this character
			int index_value_of_key = keyToNumberValue(i, key);
			// get the numeric value of the key at that index found
			int num_value_of_key = letterToNumber(key
					.charAt(index_value_of_key));

			// perform the minus calculation to get the numeric value of the
			// decrypted character
			int char_minus_key = num_value_of_char - num_value_of_key;

			// this calls the numOfPlainText method that will return an integer
			// value of the numeric value of the plain text after performing the
			// required calculations
			int plain_num_value = numOfPlainText(char_minus_key);

			// gets the character value from the numeric value of the decrypted
			// letter
			char plain_char_value = NumberToLetter(plain_num_value);
			plain_text += plain_char_value;

		}
		return plain_text;

	}

	// method to remove the spaces and other unwanted characters!
	public static String removeStuff(String S) {
		String newString = "";
		for (int i = 0; i < S.length(); i++) {
			if (S.charAt(i) != ' ' && S.charAt(i) != '.' && S.charAt(i) != ','
					&& S.charAt(i) != '!')
				newString += S.charAt(i);

		}
		return newString;
	}

	// this method is used to check if the calculated value is a negative value
	// in order to make it positive instead by adding it to 29
	// It also performs num % 29 to get the right value.
	public static int numOfPlainText(int plain_num_value) {

		int num = plain_num_value;
		if (plain_num_value < 0) {
			num = 29 + plain_num_value;
		}
		num = num % 29;
		return num;
	}

	// this method is used to return the index of the key at that point
	public static int keyToNumberValue(int index, String key) {
		int length_of_key = key.length();
		if (index >= length_of_key)
			return index % length_of_key;

		else
			return index;
	}

	// This method is used to convert each character to its corresponding number
	// value
	// so a for instance is 0, while b is 1.. and so on
	// it returns an integer value of the character
	public static int letterToNumber(char a) {
		if (a == 'å' || a == 'ö' || a == 'ä') // if it's a swedish character
		{
			if (a == 'å')
				return 26;
			if (a == 'ä')
				return 27;
			if (a == 'ö')
				return 28;
		} else { // not a Swedish Character
			return ((int) a) - 97;
		}
		return 9999;
	}

	// This method is used to convert each number value to its corresponding
	// character value
	// so 0 for instance is a, while 1 is b.. and so on
	// it returns a char value
	public static char NumberToLetter(int a) {
		if (a == 26 || a == 27 || a == 28) // if it's a swedish character
		{
			if (a == 26)
				return 'å';
			if (a == 27)
				return 'ä';
			if (a == 28)
				return 'ö';
		} else { // not a Swedish Character
			return ((char) (a + 97));
		}
		return 9999;
	}

}
