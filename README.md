App name TBD

This is meant to be a productivity tracking app that also provides readable statistics on the user's performance


# App features

## Home Screen
-home screen that displays a quote of the day via api call

-openai is preferable but costs money, so a free api called forismatic will be used instead

-home screen displays number of unfinished tasks the user has created

-home screen has buttons that lead to user's statistics and list of created tasks

## Statistics Screens
-the statistics screen will display data in a specified time frame (weekly, monthly, annual)

-statistics for:

	-how many tasks the user has marked as complete in the specified timeframe
	-the percentage completion rate of the user in the specified timeframe
	-a calculated percentage to show how much better/worse the completion rate is than the last time period
	-the last calculated completion rate from the previous time period

## Task Management Screens
-the task list screen will allow the user to view details on tasks they have created

	-task name
	-task description (abbreviated in list view)
	-specified deadline
	-completion status
 
-when the user taps on one of the tasks listed, they're taken to a screen that shows full information on the task

	-on that screen, they have options to mark the task as complete, edit the task, or delete it
 
-at the bottom of the task list is a button that allows the user to create a new task

-it takes them to a task creation screen where they can specify its properties

	-name
	-description
	-deadline

## Other Features
-ideally, the app will notify the user of upcoming deadlines and feedback on how productive they are

-the app will store information about tasks and user statistics locally

-a feature that allows sharing statistics to social media platforms will be considered
