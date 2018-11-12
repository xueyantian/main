package seedu.address.model.module;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Module's academic year in the address book.
 * Guarantees: immutable, is valid as declared in {@link #isValidYear(String)} (String)}
 */
public class AcademicYear {
    //@@author waytan
    public static final String MESSAGE_ACADEMICYEAR_CONSTRAINTS =
            "Academic Year should be a 4 digit number,"
            + " with the first and last 2 digits representing the calendar year.";

    public static final String ACADEMICYEAR_VALIDATION_REGEX = "[0-9]{4}";

    //@@author
    public final Integer yearNumber;
    // TODO: Should it be change to string?

    private boolean isEmptyYear = false;

    //@@author waytan
    /**
     * Empty constructor.
     */
    public AcademicYear() {
        yearNumber = 1718;
        isEmptyYear = true;
    }

    /**
     * Constructs a {@code AcademicYear}.
     *
     * @param number The semester number.
     */
    public AcademicYear(String number) {
        requireNonNull(number);
        checkArgument(isValidYear(number), MESSAGE_ACADEMICYEAR_CONSTRAINTS);
        yearNumber = Integer.parseInt(number);
    }

    //@@author spikerheado1234
    /**
     * Makes an identical deep copy of this academic year.
     */
    public AcademicYear makeCopy() {
        AcademicYear newYear = new AcademicYear(new String(yearNumber.toString()));
        return newYear;
    }

    //@@author waytan
    /**
     * Returns true if a given Integer is a valid AcademicYear number
     */
    public static boolean isValidYear(String number) {
        if (!number.matches(ACADEMICYEAR_VALIDATION_REGEX)) {
            return false;
        }
        Integer combinedYear = Integer.parseInt(number);
        Integer firstYear = combinedYear / 100;
        Integer secondYear = combinedYear % 100;

        // checks that the two years are consecutive
        return (firstYear + 1) % 100 == secondYear;
    }

    public Integer firstYear() {
        return yearNumber / 100;
    }

    public Integer secondYear() {
        return yearNumber % 100;
    }

    @Override
    public String toString() {
        if (isEmptyYear) {
            return "";
        }
        return String.format("%04d", yearNumber);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AcademicYear // instanceof handles nulls
                && yearNumber.equals(((AcademicYear) other).yearNumber)); // state check
    }

    @Override
    public int hashCode() {
        return yearNumber.hashCode();
    }
}
