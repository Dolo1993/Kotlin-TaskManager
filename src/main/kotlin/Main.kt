import java.io.File
import java.text.SimpleDateFormat
import java.util.*

// 
data class Task(val name: String, val dueDate: Date, var completed: Boolean = false)

// Entry point of the program.
fun main() {
    // Create a scanner to read user input.
    val scanner = Scanner(System.`in`)

    // Create a mutable list to store tasks.
    val tasks = mutableListOf<Task>()

    // Create a date formatter for "yyyy-MM-dd" format.
    val dateFormat = SimpleDateFormat("yyyy-MM-dd")

    // Load tasks from a file (if it exists) into the tasks list.
    loadTasksFromFile(tasks, dateFormat)

    // Start an infinite loop to display the menu and handle user input.
    while (true) {
         printMenu()

        // Read the user's choice from input.
        val choice = readIntInput(scanner)

        // Handle the user's choice using a when expression.
        when (choice) {
            // Case 1: Add a new task.
            1 -> {
                println("Enter the task name:")
                val name = scanner.next()
                println("Enter the due date (yyyy-MM-dd):")
                val dueDateStr = scanner.next()
                val dueDate = dateFormat.parse(dueDateStr)
                tasks.add(Task(name, dueDate))
                println("Task added: $name")
            }
            // Case 2: List tasks.
            2 -> {
                listTasks(tasks)
            }
            // Case 3: Remove a task.
            3 -> {
                // Check if the tasks list is empty.
                if (tasks.isEmpty()) {
                    println("No tasks to remove.")
                } else {
                    println("Enter the task number to remove:")
                    val taskNumber = readIntInput(scanner)
                    // Check if the entered task number is valid.
                    if (taskNumber in 1..tasks.size) {
                        val removedTask = tasks.removeAt(taskNumber - 1)
                        println("Removed task: ${removedTask.name}")
                    } else {
                        println("Invalid task number.")
                    }
                }
            }
            // Case 4: Mark a task as completed.
            4 -> {
                // Check if the tasks list is empty.
                if (tasks.isEmpty()) {
                    println("No tasks to mark as completed.")
                } else {
                    listTasks(tasks)
                    println("Enter the task number to mark as completed:")
                    val taskNumber = readIntInput(scanner)
                    // Check if the entered task number is valid.
                    if (taskNumber in 1..tasks.size) {
                        val task = tasks[taskNumber - 1]
                        task.completed = true
                        println("Task marked as completed: ${task.name}")
                    } else {
                        println("Invalid task number.")
                    }
                }
            }
            // Case 5: Save tasks to a file.
            5 -> {
                saveTasksToFile(tasks, dateFormat)
                println("Tasks saved to file.")
            }
            // Case 6: Exit the program.
            6 -> {
                println("Exiting Task Manager. Goodbye!")
                return
            }
            // Default case: Invalid choice.
            else -> {
                println("Invalid choice. Please try again.")
            }
        }
    }
}

// Function to display the menu.
fun printMenu() {
    println("Task Manager")
    println("1. Add a task")
    println("2. List tasks")
    println("3. Remove a task")
    println("4. Mark task as completed")
    println("5. Save tasks to file")
    println("6. Exit")
    print("Enter your choice: ")
}

// Function to list tasks.
fun listTasks(tasks: List<Task>) {
    if (tasks.isEmpty()) {
        println("No tasks found.")
    } else {
        println("Tasks:")
        // Iterate through tasks and display their details.
        for ((index, task) in tasks.withIndex()) {
            val status = if (task.completed) "[Completed]" else "[Pending]"
            println("${index + 1}. $status ${task.name} (Due: ${task.dueDate})")
        }
    }
}

// Function to load tasks from a file.
fun loadTasksFromFile(tasks: MutableList<Task>, dateFormat: SimpleDateFormat) {
    // Create a file object for "tasks.txt".
    val file = File("tasks.txt")
    // Check if the file exists.
    if (file.exists()) {
        // Read each line from the file and parse it into tasks.
        file.forEachLine { line ->
            val parts = line.split("|")
            if (parts.size == 3) {
                val name = parts[0]
                val dueDate = dateFormat.parse(parts[1])
                val completed = parts[2].toBoolean()
                tasks.add(Task(name, dueDate, completed))
            }
        }
    }
}

// Function to save tasks to a file.
fun saveTasksToFile(tasks: List<Task>, dateFormat: SimpleDateFormat) {
    // Create a file object for "tasks.txt".
    val file = File("tasks.txt")
    // Use a print writer to write tasks to the file.
    file.printWriter().use { out ->
        tasks.forEach { task ->
            val completed = if (task.completed) "true" else "false"
            out.println("${task.name}|${dateFormat.format(task.dueDate)}|$completed")
        }
    }
}

// Define a function to read integer input and handle exceptions
fun readIntInput(scanner: Scanner): Int {
    while (true) {
        try {
            return scanner.nextInt()
        } catch (e: InputMismatchException) {
            println("Invalid input. Please enter a valid integer.")
            // Clear the invalid input to prevent an infinite loop.
            scanner.next()
        }
    }
}
