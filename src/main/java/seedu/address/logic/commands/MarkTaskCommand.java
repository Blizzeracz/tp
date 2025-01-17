package seedu.address.logic.commands;

import static java.util.Arrays.stream;
import static java.util.Objects.requireNonNull;
import static seedu.address.logic.AnimalMessages.MESSAGE_INVALID_ANIMAL_DISPLAYED_INDEX;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AnimalModel;
import seedu.address.model.animal.AdmissionDate;
import seedu.address.model.animal.Animal;
import seedu.address.model.animal.Breed;
import seedu.address.model.animal.DateOfBirth;
import seedu.address.model.animal.Name;
import seedu.address.model.animal.PetId;
import seedu.address.model.animal.Sex;
import seedu.address.model.animal.Species;
import seedu.address.model.animal.TaskList;

/**
 * Marks a task of an animal as done.
 */
public class MarkTaskCommand extends AnimalCommand {

    public static final String COMMAND_WORD = "mark";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Marks a task of the animal, identified by the index number "
            + "used in the displayed animal list, as done. "
            + "\nParameters: ANIMAL_INDEX (must be a positive integer)"
            + "TASK_INDEX (must be a positive integer)";

    public static final String EXAMPLE_USAGE = "Example: "
            + COMMAND_WORD
            + " 1 "
            + "1 2 3";

    public static final String MESSAGE_SUCCESS = "Task(s) marked as done";

    public static final String MESSAGE_INVALID_TASK_DISPLAYED_INDEX = "The task index provided is invalid";

    private final Index targetIndex;
    private final Index[] taskIndex;

    /**
     * Creates a MarkTaskCommand to mark the specified {@code Task} of the {@code Animal} at the specified
     * {@code Index}.
     */
    public MarkTaskCommand(Index targetIndex, Index... taskIndex) {
        this.targetIndex = targetIndex;
        this.taskIndex = taskIndex;
    }

    @Override
    public CommandResult execute(AnimalModel model) throws CommandException {
        requireNonNull(model);
        List<Animal> lastShownList = model.getFilteredAnimalList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(MESSAGE_INVALID_ANIMAL_DISPLAYED_INDEX);
        }

        Animal animalToMark = lastShownList.get(targetIndex.getZeroBased());
        try {
            int[] taskIndexes = stream(taskIndex)
                    .map(Index::getZeroBased)
                    .mapToInt(Integer::intValue)
                    .toArray();

            Animal markedAnimal = createEditedAnimal(animalToMark, taskIndexes);

            model.setAnimal(animalToMark, markedAnimal);
            model.updateFilteredAnimalList(AnimalModel.PREDICATE_SHOW_ALL_ANIMALS);

            return new CommandResult(MESSAGE_SUCCESS);
        } catch (IndexOutOfBoundsException e) {
            throw new CommandException(MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
        }
    }

    private static Animal createEditedAnimal(Animal animalToMark, int[] taskIndex)
            throws IndexOutOfBoundsException {
        Name name = animalToMark.getName();
        PetId petId = animalToMark.getPetId();
        Species species = animalToMark.getSpecies();
        Breed breed = animalToMark.getBreed();
        Sex sex = animalToMark.getSex();
        AdmissionDate admissionDate = animalToMark.getAdmissionDate();
        DateOfBirth dateOfBirth = animalToMark.getDateOfBirth();
        TaskList taskList = animalToMark.updateTaskList(taskIndex, true);

        return new Animal(name, petId, species, breed, sex, admissionDate, dateOfBirth, taskList);
    }
}
