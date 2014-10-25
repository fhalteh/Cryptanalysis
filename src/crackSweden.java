import java.util.Scanner;

public class crackSweden {

	public static void main(String[] args) {
		long startTime = System.nanoTime();

		Scanner scan = new Scanner(System.in);
		boolean stopprogram = false;

		do {
			// Ask the user for the cipher text input
			System.out
					.println("Please enter the text that you would like to decrypt using Vigenere cipher: ");
			String ciphertext = scan.next();

			// convert the cipher text to Lower case in case it has any
			// Capitalized
			// letters.
			ciphertext = ciphertext.toLowerCase();

			int length; // length of the combination
			int spacing; // distance between 2 of the same substrings.

			char[][] char_matrix; // double array that will store the letters of
									// the
									// cipher text segmented according to the
									// guessed key length.
			int[] spacingMod = new int[40]; // an array that will store the
											// spacing
			char[] key_letters;
			spacingMod = initializeArray(spacingMod);
			// Kasiski method was used in order to find out the length of the
			// key!
			// Check for all possible lengths:
			for (length = 3; length < 10; length++) {
				// Take the combination:
				for (int i = 0; i < ciphertext.length() - (length - 1); i++) {
					String combination = ciphertext.substring(i, i + length);
					// Take the next combination
					for (int j = (i + 1); j < ciphertext.length()
							- (length - 1); j++) {
						String other_combination = ciphertext.substring(j, j
								+ length);
						// Compare the combinations and calculate the spacing
						// between them.
						if (other_combination.equalsIgnoreCase(combination)) {
							spacing = (j - i);
							spacingMod = checkMod(spacing, spacingMod);
							// System.out.println(combination + " Spacing: " +
							// (j-i)
							// );
						}
					}
				}

			}

			// print out the spacing between
			System.out.println();
			System.out.println("Key Length" + "\t" + "Frequency");
			for (int i = 2; i < spacingMod.length; i++) {
				System.out.println(i + "\t \t" + spacingMod[i]);
			}

			System.out.println();
			// Asks the user for a possible key length after printing out a list
			// of
			// the numbers corresponding to the most common spacing factor to
			// identify the length of the keyword. Having established the length
			// of
			// the keyword, we will move to the next stage of cracking the
			// cipher.
			System.out
					.println("Please write a number that corresponds to the most common spacing factor to identify the length of the keyword: ");
			int keylength = scan.nextInt();
			char_matrix = splittingUpCiphertext(keylength, ciphertext);

			// initialize the array that holds the key letters using the key
			// length.
			key_letters = new char[keylength];
			key_letters = analyseFrequency(char_matrix, key_letters);
			System.out.println();
			System.out.println("Printing the key letters: ");

			String key = "";
			for (int i = 0; i < key_letters.length; i++) {
				key += key_letters[i];
				System.out.print(key_letters[i]);
			}

			System.out.println();
			decryptWithKey decrypter = new decryptWithKey(key, ciphertext);

			long endTime = System.nanoTime();
			System.out.println("Took " + (endTime - startTime) + " ns");

			System.out.println();
			System.out
					.println("Would you like to attempt to decrypt another text? Please press 1 for yes, and 2 to terminate this program.");
			int answer = scan.nextInt();
			if (answer == 2)
				stopprogram = true;
		} while (stopprogram == false);
	}

	// This method is used to analyse the frequency of the characters that have
	// been
	// encrypted using the same letter of the key.
	// it takes the char_matrix double array that stores all the letters of the
	// cipher
	// segmented according to the key length and the key letters as parameters.
	// this method returns an array of characters that are assumed to resemble
	// the key
	// after analysing the double array.
	private static char[] analyseFrequency(char[][] char_matrix,
			char[] key_letters) {

		// this for loop loops over the columns of char_matrix to perform
		// frequency analysis on each column
		for (int flag = 0; flag < char_matrix[0].length; flag++) {
			int[] alphabet_frequency = new int[29];

			// array initialized to 0 values
			alphabet_frequency = initializeArray(alphabet_frequency);

			// System.out.println("This is the length: " + char_matrix.length);

			// this for loop loops over the rows of the double array in order to
			// get all the characters that have been encrypted with the same
			// character in the key
			// if we take the corresponding letters in the ciphertext, we know
			// they have been encrypted using the same row of the Vigenère
			// square, and we can work out which row by using frequency
			// analysis, because each row of the square is equivalent to one
			// monoalphabetic cipher.
			for (int row = 0; row < char_matrix.length; row++) {
				char current_character = ' ';
				// we need to check if this current letter is swedish and not a
				// null space in the array in order to avoid having errors
				if (checkIfLetterIsSwedish(char_matrix[row][flag])) {
					// current_character stores the character at that point in
					// the double array
					current_character = char_matrix[row][flag];
					// System.out.println("This is the character at " + row +
					// ": "
					// + current_character);

					// After getting the current character, we want to get its
					// numeric value in order to keep track of the frequency of
					// this character using the alphabet frequency array
					int num = letterToNumber(current_character);
					alphabet_frequency[num]++;
				}
			}

			int sum_of_letters = 0;
			// This loop is used to go through all the alphabets to show the
			// frequency of that particular letter
			for (int i = 0; i < alphabet_frequency.length; i++) {
				System.out.println("Letter " + NumberToLetter(i) + ":" + " "
						+ alphabet_frequency[i] + "  ");
				sum_of_letters += alphabet_frequency[i];
			}

			// an array to keep track of the percentage of each letter
			double[] percentages = new double[alphabet_frequency.length];
			for (int i = 0; i < percentages.length; i++) {
				percentages[i] = (alphabet_frequency[i] / sum_of_letters) * 100;
			}

			double[] svenskaletters = { 9.3, 1.3, 1.3, 4.5, 9.9, 2, 3.3, 2.1,
					5.1, 0.7, 3.2, 5.2, 3.5, 8.8, 4.1, 1.7, 0.007, 8.3, 6.3,
					8.7, 1.8, 2.4, 0.03, 0.1, 0.6, 0.02, 1.6, 2.1, 1.5 };

			// difference array to store all the differences between the swedish
			// letters and our letters
			double[] differences = new double[svenskaletters.length];
			differences = initializeArray(differences);
			for (int j = 0; j < differences.length; j++) {
				for (int i = 0; i < percentages.length; i++) {
					differences[j] = differences[j]
							/************* we need to shift the percentages j back!! ***/
							+ Math.pow((svenskaletters[i] - percentages[i]), 2);
				}
				System.out.println("The difference at position" + j + " is " + differences[j]);
			}
			
			//find minimal difference in the difference array
			double minimum = differences[0];
			int index_of_minimum = 0;
			for (int i = 1; i < differences.length;i ++){
				if ( differences[i]<minimum){
					minimum = differences [ i ];
					index_of_minimum = i;
					System.out.println("the minimum is: " + minimum + " on index: " + i);}
			}
			
			char letter = NumberToLetter(index_of_minimum);
			
			/*
						
			char frequent_letter = NumberToLetter(indexOfMostFrequentLetter(alphabet_frequency));
			System.out.println();

			// now that we have a frequent letter calculated, we want to ask the
			// user if he or she agrees as well
			System.out
					.println("It is suggested that "
							+ frequent_letter
							+ " is the most frequent letter, please press 1 to go on, and press 2 to enter something else:");
			Scanner scan = new Scanner(System.in);
			int answer = scan.nextInt();
			if (answer == 2) {
				System.out.println("Enter your suggested letter: ");
				frequent_letter = scan.next().charAt(0);
				System.out.println("Character: " + frequent_letter);
			}

			*/
			
			key_letters[flag] = letter;

			// calculateKeyLetter method is called in order to do a shift
			// calculation to
			// get a letter in the key.
			//char key_letter = calculateKeyLetter(frequent_letter);
			//key_letters[flag] = key_letter;
		}
		return key_letters;
	}

	private static double[] initializeArray(double[] differences) {
		for (int i = 0; i < differences.length; i++) {
			differences[i] = 0;
		}
		return differences;
	}

	// this method checks if the character is a letter in the swedish language
	// returns true if it's a swedish character, otherwise false if it's not.
	public static boolean checkIfLetterIsSwedish(char a) {
		if (a == 'å' || a == 'Å' || a == 'ö' || a == 'Ö' || a == 'ä'
				|| a == 'Ä' || (a > 'a' && a < 'z') || (a > 'A' && a < 'Z')) {
			return true;
		}
		return false;
	}

	// this method is used to return a character in the key using a shift
	// calculation on the most
	// frequent letter in the cipher text that corresponds to the most frequent
	// letter in teh swedish letters.
	public static char calculateKeyLetter(char most_frequent_letter) {
		// most frequent letter in usual Swedish text (plain text)
		char e = 'e';
		int shift = (letterToNumber(most_frequent_letter) - letterToNumber(e));
		if (shift < 0) {
			shift = 29 + shift;
		}
		shift = shift % 29;
		char x = NumberToLetter(shift);
		return x;
	}

	// this method loops through an array
	// it returns the index that has the highest value in the array
	public static int indexOfMostFrequentLetter(int[] array) {
		int max_index = 0;
		for (int i = 1; i < array.length; i++) {
			if (array[i] > array[max_index])
				max_index = i;
		}
		return max_index;

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

	// this method takes as input the keylength and cipher text
	// it's used to split up the cipher text into segments of length (key
	// length)
	// and store the letters in a double array
	// this method returns a double array with the stored letters.
	private static char[][] splittingUpCiphertext(int keylength,
			String ciphertext) {
		int height = (int) Math.ceil(ciphertext.length() / keylength) + 1;
		char[][] char_matrix = new char[height][keylength];
		int flag = 0;
		for (int row = 0; row < height; row++) {
			for (int col = 0; col < keylength && flag < ciphertext.length(); col++) {
				char_matrix[row][col] = ciphertext.charAt(flag);
				// System.out.print(char_matrix[row][col] + "\t");
				flag++;
			}
			System.out.println();
		}

		return char_matrix;

	}

	// this method is used to check the modulus of the spacing with the index
	// if the index is a factor of the spacing, then one is added to the value
	// of that
	// index in the array.
	// f one factor is common to many of the spacing, then this is probably the
	// length of the keyword.
	// this method returns an array of integers that represent the factors of
	// that specific spacing
	private static int[] checkMod(int spacing, int[] spacingMod) {
		for (int i = 2; i < spacingMod.length; i++) {
			if (spacing % i == 0) {
				spacingMod[i]++;
			}
		}
		return spacingMod;
	}

	// method used to initialize an int array with 0 values.
	public static int[] initializeArray(int[] spacingMod) {
		for (int i = 0; i < spacingMod.length; i++)
			spacingMod[i] = 0;

		return spacingMod;
	}

}
