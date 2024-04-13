package com.example;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

public class Main {
    //Alter to your specs
    static String url = "jdbc:postgresql://localhost:5432/University";
    static String user = "postgres";
    static String password = "1234";
    
    public static void main(String[] args) {
        startupMenu();
        //connect();
    }

//SQL Connection
    public static Connection connect() {
        try {
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection(url, user, password);
            /* 
            if (connection != null) {
            //System.out.println("Connected to Database");
            } else {
            System.out.println("Failed to connect to Datebase");
            }
            */
            return connection;
        } catch (Exception var1) {
            System.out.println("Failed to connect to Datebase");
            return null;
        }
   }

//SQL Updates
    public static void login() {
        //Page Title
        clearScreen();
        System.out.println("USER LOGIN\n");
        
        //Login info
        System.out.print("Username: ");
        String Username = input();
        System.out.print("Password: ");
        String password = input();
        String firstName;
        String lastName;
        String userType;
        //Sql connection
        Connection connection = connect();
        try {
            Statement statement = connection.createStatement();
            //statement.executeQuery("Select * from public.users where user_email='"+Username+"' and password='"+password+"'");
            statement.executeQuery("SELECT * FROM public.users WHERE user_email='"+Username+"' AND password='"+password+"'");
            ResultSet resultSet = statement.getResultSet();
            resultSet.next();
            firstName=resultSet.getString("first_name");
            lastName=resultSet.getString("last_name");
            userType=resultSet.getString("account_type");
            
        } catch (Exception var6) {
            System.out.println("Failed to login");
            pause();
            return;
        }
        //redirection
        if(!firstName.equals(null)) mainMenu(Username, firstName,lastName,userType);
    }
    
    public static void updateInfo(String userName,String Firstname, String Lastname){
        //Gets User Info
        Connection connection = connect();
        String sex,type="",signupDate ="";
        try {
            Statement statement = connection.createStatement();
            //statement.executeQuery("Select * from public.users where user_email='"+Username+"' and password='"+password+"'");
            statement.executeQuery("SELECT * FROM public.users WHERE user_email='"+userName+"'");
            ResultSet resultSet = statement.getResultSet();
            resultSet.next();
            sex=resultSet.getString("sex");
            type=resultSet.getString("account_type");
            signupDate=resultSet.getString("signup_date");
            
        } catch (Exception var6) {
            System.out.println("Failed to connect");
            pause();
            return;
        }

        
        while(true) {
            clearScreen();
            System.out.println("PERSONAL INFO MENU");
            System.out.println(Firstname+ " "+Lastname+"    Username: "+userName+"    Sex: "+sex+"    Account Type: "+type+"    Start Date: "+signupDate+"\n");
            System.out.println("1: Update First Name");
            System.out.println("2: Update Last Name");
            System.out.println("3: Update Password");
            System.out.println("4: Back");
            String Responce = input();
            if (Responce.equals("1")) {
                System.out.println("Enter new first name: ");
                Responce = input();
                Firstname=Responce;
                patch("update users set first_name='"+Responce+"' where user_email='"+userName+"'");
            } else if (Responce.equals("2")) {
                System.out.println("Enter new last name: ");
                Responce = input();
                Lastname = Responce;
                patch("update users set last_name='"+Responce+"' where user_email='"+userName+"'");
            } else if (Responce.equals("3")) {
                System.out.println("Enter new password: ");
                Responce = input();
                patch("update users set password='"+Responce+"' where user_email='"+userName+"'");
            } else {
                if (Responce.equals("4")) {
                    return;
                }
                System.out.println("Need to enter a number between 1-4");
                pause();
            }
            mainMenu(userName,Firstname, Lastname,type);
        }
    }

    public static void updateFitnessGoal(String userName){
        int start=0, current =0;
        while(true){
        String statment = "SELECT * FROM public.fitness_goals where user_email='"+userName+"' ORDER BY goal_id ASC";
        ArrayList<String> colums = new ArrayList<String>();
        colums.add("goal_id");
        colums.add("goal");
        colums.add("completion_date");
        ArrayList<String> goals = interactItem(statment,colums);
        
            clearScreen();
            System.out.println("UPDATE FITNESS GOALS\n");
            System.out.printf("%-7s %-20s %s\n","Index","Date Completed","Goal");
            while(current<5&&start+current<goals.size()){
                String[] arrOfStr = goals.get(start+current).split(", ", 3);
                System.out.printf("%-7d %-20s %s\n",start+current+1,arrOfStr[2],arrOfStr[1]);
                current++;
            }
            current =0;
            System.out.println("Enter: p - predvous      n - next    # - goal to update    q - quit");
            String input = input().toLowerCase();
            if (input.equals("p")) {
                if(start-5>=0) start-=5;
            }else if (input.equals("n")) {
                if(start+5<goals.size()) start+=5;
            }else if (input.equals("q")) {
                return;
            }else{
                try {
                    clearScreen();
                    int index = Integer.parseInt(input);
                    String[] arrOfStr = goals.get(index-1).split(", ", 3);
                    System.out.printf("%-20s %s\n",arrOfStr[2],arrOfStr[1]);
                    System.out.println("Enter: d - delete      c - complete    q - quit");
                    input = input().toLowerCase();
                    if (input.equals("d")) {
                        patch("delete from public.fitness_goals where user_email= '"+userName+"' and goal_id="+arrOfStr[0]);
                        return;
                    }else if (input.equals("c")) {
                        patch("update public.fitness_goals set completion_date='"+java.time.LocalDate.now()+"' where user_email= '"+userName+"' and goal_id="+arrOfStr[0]);
                        return;
                    }else if (input.equals("q")) {
                        return;
                    }
                } catch (Exception e) {
                }
            }
        }


    }
    
    public static void updateEquipment(String equipment){
        clearScreen();
        System.out.println("Enter name");
        String name = input();
        System.out.println("Enter room #");
        String room = input();
        System.out.println("Enter open(true,false)");
        String open = input();

        patch("Update equipment set name='"+name+"', open='"+open+"', room_num="+room+" where equipment_id="+equipment);
    }
    
    public static void updateSession(String[] arrOfStr){
        clearScreen();
        System.out.println("UPDATE SESSION\n");
        System.out.printf("%18s %15s %15s %15s %8s %8s\n","Session Size","Date","Start Time","End Time","Room #","Cost");
        System.out.printf("%18s %15s %15s %15s %8s %8s\n\n",arrOfStr[1],arrOfStr[3],arrOfStr[4],arrOfStr[5],arrOfStr[6],arrOfStr[7]);

        //new user info
        System.out.print("Max Session Size: ");
        String size = input();
        System.out.print("Date (YYYY-MM-DD): ");
        String date = input();
        System.out.print("Start Time (HH:MM): ");
        String start = input();
        System.out.print("End Time (HH:MM): ");
        String end = input();
        System.out.print("Room #: ");
        String room = input();
        System.out.print("Cost: ");
        String cost = input();

        patch("UPDATE session set max_size="+size+", date='"+date+"', start_time='"+start+"', end_time='"+end+"', room_num="+room+", cost="+cost+" where session_id="+arrOfStr[0]);
    }
    
    //Sql Inserts
    public static void addUser() {
        clearScreen();
        System.out.println("ADD NEW USER\n");

        //new user info
        System.out.print("Username/email: ");
        String userName = input();
        System.out.print("First name: ");
        String firstName = input();
        System.out.print("Last name: ");
        String lastName = input();
        String sex;
        do {
            System.out.print("Sex (M/F/O): ");
            sex = input();
            if(sex.equals("M")||sex.equals("F")||sex.equals("O")) break;
        } while (true);
        System.out.print("Password: ");
        String password = input();

        patch("INSERT INTO users (user_email, first_name,last_name, account_type, sex, password) VALUES\r\n('" + userName + "', '" + firstName + "', '" + lastName + "', 'member', '"+ sex + "', '" + password + "');");

        //redirection
        mainMenu(userName, firstName, lastName,"Member");
    }

    public static void addFitnessGoal(String userName){
        System.out.println("Enter a fitness goal");
        String Responce = input();
        patch("INSERT INTO fitness_goals (user_email, goal) VALUES ('" + userName + "', '" + Responce + "');");
    }

    public static void addExerciseRoutine(String userName){
        clearScreen();
        System.out.println("Enter exercise to add to your routine");
        String Responce = input();
        patch("INSERT INTO exercise_routines (user_email, routine) VALUES\r\n('" + userName + "', '" + Responce + "');");
    }

    public static void addHealthMetic(String userName){
        clearScreen();
        System.out.println("Enter body weight(kg)");
        String weight = input();
        System.out.println("Enter hight(cm)");
        String height = input();
        System.out.println("Enter averge heartrate(bpm)");
        String bpm = input();
        System.out.println("Enter BMI");
        String bmi = input();
        patch("INSERT INTO health_metrics (user_email, weight,height,heart_rate,bmi) VALUES\r\n('" + userName + "', '" + weight +"', '" + height +"', '" + bpm +"', '" + bmi + "');");
    }

    public static void addSession(String userName){
        clearScreen();
        System.out.println("ADD NEW SESSION\n");

        //new user info
        System.out.print("Max Session Size: ");
        String size = input();
        System.out.print("Date (YYYY-MM-DD): ");
        String date = input();
        System.out.print("Start Time (HH:MM): ");
        String start = input();
        System.out.print("End Time (HH:MM): ");
        String end = input();
        System.out.print("Room #: ");
        String room = input();
        System.out.print("Cost: ");
        String cost = input();

        patch("INSERT INTO session (user_email, max_size,date,start_time, end_time, room_num, cost) VALUES\r\n('" + userName + "', '" + size + "', '" + date + "', '" + start + "', '"+end+"', '"+ room + "', '" + cost + "');");
    }
    
    public static void addEquipment(){
        clearScreen();
        System.out.println("Enter name");
        String name = input();
        System.out.println("Enter room #");
        String room = input();
        System.out.println("Enter open(true,false)");
        String open = input();

        patch("INSERT INTO equipment (name, open,room_num) VALUES\r\n('" + name +"', '" + open +"', '" + room+"');");
    }
    
    public static void addEquipmentMaintenance(String userName,String equipment){
        clearScreen();
        System.out.println("Enter Comments");
        String comments = input();
        patch("INSERT INTO equipment_maintenance (equipment_id,comments,user_email) VALUES\r\n(" + Integer.valueOf(equipment) +", '" + comments +"', '" + userName+"');");
    }

    //data displays
    public static void viewExerciseRoutine(String userName){
        clearScreen();
        System.out.println("EXERCISE ROUTINE");
        Connection connection = connect();
        try {
            Statement statement = connection.createStatement();
            statement.executeQuery("SELECT * FROM public.exercise_routines where user_email='"+userName+"'");
            ResultSet resultSet = statement.getResultSet();
            while(resultSet.next()){
                System.out.println(resultSet.getString("routine"));
            }
            System.out.println("\nEnter to return");
            input();
        } catch (Exception var6) {
            return;
        }
    }

    public static void viewFitnessAchievements(String userName){
        clearScreen();
        System.out.println("FITNESS ACHIEVEMENTS\n");
        Connection connection = connect();
        try {
            Statement statement = connection.createStatement();
            statement.executeQuery("SELECT * FROM public.fitness_goals where user_email='"+userName+"' and completion_date is not NULL ORDER BY completion_date ASC ");
            ResultSet resultSet = statement.getResultSet();
            System.out.printf("%-20s %s\n","Date Completed","Goal");
            while(resultSet.next()){
                System.out.printf("%-20s %s\n",resultSet.getString("completion_date"),resultSet.getString("goal"));
            }
            System.out.println("\nEnter to return");
            input();
        } catch (Exception var6) {
            return;
        }
    }

    public static void viewHealthStatistics(String userName){
        clearScreen();
        System.out.println("HEALTH STATISTICS");
        Connection connection = connect();
        try {
            Statement statement = connection.createStatement();
            statement.executeQuery("SELECT * FROM public.health_metrics where user_email='"+userName+"'");
            ResultSet resultSet = statement.getResultSet();
            while(resultSet.next()){
                if (resultSet.isFirst()||resultSet.isLast()) {
                    if (resultSet.isFirst()) System.out.printf("\n%-10s","Starting:");
                    else if (resultSet.isLast()) System.out.printf("\n%-10s","Current:");
                    System.out.printf(" Weight: %s Height: %s BPM: %s BMI: %s Date: %s",resultSet.getString("weight"),resultSet.getString("height"),
                    resultSet.getString("heart_rate"),resultSet.getString("bmi"),resultSet.getString("input_date"));
                }
            }
            statement.executeQuery("SELECT avg(weight) as weight,avg(height)as height,avg(heart_rate)as heart_rate,avg(bmi)as bmi FROM public.health_metrics where user_email='"+userName+"'");
            resultSet = statement.getResultSet();
            while(resultSet.next()){
                if (resultSet.isFirst()||resultSet.isLast()) {
                    System.out.printf("\n%-10s Weight: %s Height: %s BPM: %s BMI: %s\n","Average: ",resultSet.getInt("weight"),resultSet.getInt("height"),
                    resultSet.getInt("heart_rate"),resultSet.getInt("bmi"));
                }
            }
            System.out.println("\nEnter to return");
            input();
        } catch (Exception var6) {
            return;
        }
    }
    
    //view a singler sessions and its members admins also use
    public static void viewSessionTrainer(String userName,String[] arrOfStr){
        clearScreen();
        System.out.println("EDIT SESSION\n");
        System.out.printf("%18s %15s %15s %15s %8s %8s\n","Session Size","Date","Start Time","End Time","Room #","Cost");
        System.out.printf("%18s %15s %15s %15s %8s %8s\n\n",arrOfStr[1],arrOfStr[3],arrOfStr[4],arrOfStr[5],arrOfStr[6],arrOfStr[7]);
        int start=0, current=0;
        while(true){
            String statment = "Select users.first_name,users.last_name, session_members.user_email, paid from session_members inner join users on users.user_email= session_members.user_email where session_id="+arrOfStr[0]+" ORDER BY last_name ASC";
            ArrayList<String> colums = new ArrayList<String>();
            colums.add("first_name");
            colums.add("last_name");
            colums.add("user_email");
            colums.add("paid");
            ArrayList<String> goals = interactItem(statment,colums);
            

            System.out.printf("%20s %20s %20s %8s\n","First Name","Last Name","Email","Paid");
            while(current<5&&start+current<goals.size()){
                String[] arrOfStr2 = goals.get(start+current).split(", ", 10);
                System.out.printf("%20s %20s %20s %8s\n",arrOfStr2[0],arrOfStr2[1],arrOfStr2[2],arrOfStr2[3]);
                current++;
            }
            current =0;
            System.out.println("\nEnter: p - predvous    n - next    u - update d - delete   q - quit");
            String input = input().toLowerCase();
            if (input.equals("p")) {
                if(start-5>=0) start-=5;
            }else if (input.equals("n")) {
                if(start+5<goals.size()) start+=5;
            }else if (input.equals("u")) {
                updateSession(arrOfStr);
                return;
            }else if (input.equals("q")) {
                break;
            }else if (input.equals("d")) {
                patch("delete from public.session_members where session_id= "+arrOfStr[0]);
                patch("delete from public.session where session_id= "+arrOfStr[0]);
                break;
            }
        }
        return;
    }

    public static void viewMySessionsTrainer(String userName){
        int start=0, current=0;
        while(true){
            String statment = "SELECT * FROM public.session where user_email='"+userName+"' ORDER BY date ASC";
            ArrayList<String> colums = new ArrayList<String>();
            colums.add("session_id");
            colums.add("max_size");
            colums.add("date");
            colums.add("start_time");
            colums.add("end_time");
            colums.add("room_num");
            colums.add("cost");
            ArrayList<String> goals = interactItem(statment,colums);
            

            clearScreen();
            System.out.println(start+"MY TRAINER SESSIONS\n");
            System.out.printf("%8s %18s %15s %15s %15s %8s %8s\n","Index","Session Size","Date","Start Time","End Time","Room #","Cost");
            while(current<5&&start+current<goals.size()){
                String[] arrOfStr = goals.get(start+current).split(", ", 10);
                System.out.printf("%8d %18s %15s %15s %15s %8s %8s\n",start+current+1,arrOfStr[1],arrOfStr[2],arrOfStr[3],arrOfStr[4],arrOfStr[5],arrOfStr[6]);
                current++;
            }
            current =0;
            System.out.println("\nEnter: p - predvous    n - next    # - session to veiw    c - create    q - quit");
            String input = input().toLowerCase();
            if (input.equals("p")) {
                if(start-5>=0) start-=5;
            }else if (input.equals("n")) {
                if(start+5<goals.size()) start+=5;
            }else if (input.equals("q")) {
                return;
            }else if (input.equals("c")) {
                addSession(userName);
            }else{
                try {
                    int index = Integer.parseInt(input);
                    String[] arrOfStr = goals.get(index-1).split(", ", 10);
                    viewSessionTrainer(userName, arrOfStr);
                } catch (Exception e) {
                }
            }
        }
    }
    
    //sessions open to members
    public static void viewSessionsMember(String userName){
        int start=0, current=0;
        while(true){
            String statment = "SELECT session.session_id,session.max_size, count(session_members.session_id),session.date,session.start_time,session.end_time,session.room_num,session.cost "+
            "FROM session "+
            "left join session_members on session.session_id= session_members.session_id "+
            "group by session.session_id  ORDER BY date ASC";
            ArrayList<String> colums = new ArrayList<String>();
            colums.add("session_id");
            colums.add("max_size");
            colums.add("count");
            colums.add("date");
            colums.add("start_time");
            colums.add("end_time");
            colums.add("room_num");
            colums.add("cost");
            ArrayList<String> goals = interactItem(statment,colums);
            

            clearScreen();
            System.out.println("JOIN SESSIONS\n");
            System.out.printf("%8s %18s %18s %15s %15s %15s %8s %8s\n","Index","Max Session Size","Session Size","Date","Start Time","End Time","Room #","Cost");
            while(current<5&&start+current<goals.size()){
                String[] arrOfStr = goals.get(start+current).split(", ", 10);
                System.out.printf("%8d %18s %18s %15s %15s %15s %8s %8s\n",start+current+1,arrOfStr[1],arrOfStr[2],arrOfStr[3],arrOfStr[4],arrOfStr[5],arrOfStr[6],arrOfStr[7]);
                current++;
            }
            current =0;
            System.out.println("\nEnter: p - predvous    n - next    # - session to join    q - quit");
            String input = input().toLowerCase();
            if (input.equals("p")) {
                if(start-5>=0) start-=5;
            }else if (input.equals("n")) {
                if(start+5<goals.size()) start+=5;
            }else if (input.equals("q")) {
                return;
            }else{
                try {
                    int index = Integer.parseInt(input);
                    String[] arrOfStr2 = goals.get(index-1).split(", ", 10);
                    if(Integer.valueOf(arrOfStr2[1])>Integer.valueOf(arrOfStr2[2])){
                        patch("insert  into session_members (session_id,user_email) values ("+arrOfStr2[0]+",'"+userName+"')");
                        System.out.println("Joined Session");
                        pause();
                    }else{
                        System.out.println("Session Full");
                        pause();
                    }
                    
                } catch (Exception e) {
                }
            }
        }
    }
    
    public static void viewSessionsAdmin(String userName){
        int start=0, current=0;
        while(true){
            String statment = "SELECT session.session_id,session.max_size, count(session_members.session_id),session.date,session.start_time,session.end_time,session.room_num,session.cost "+
            "FROM session "+
            "left join session_members on session.session_id= session_members.session_id "+
            "group by session.session_id  ORDER BY date ASC";
            ArrayList<String> colums = new ArrayList<String>();
            colums.add("session_id");
            colums.add("max_size");
            colums.add("count");
            colums.add("date");
            colums.add("start_time");
            colums.add("end_time");
            colums.add("room_num");
            colums.add("cost");
            ArrayList<String> goals = interactItem(statment,colums);
            

            clearScreen();
            System.out.println("ALL SESSIONS\n");
            System.out.printf("%8s %18s %18s %15s %15s %15s %8s %8s\n","Index","Max Session Size","Session Size","Date","Start Time","End Time","Room #","Cost");
            while(current<5&&start+current<goals.size()){
                String[] arrOfStr = goals.get(start+current).split(", ", 10);
                System.out.printf("%8d %18s %18s %15s %15s %15s %8s %8s\n",start+current+1,arrOfStr[1],arrOfStr[2],arrOfStr[3],arrOfStr[4],arrOfStr[5],arrOfStr[6],arrOfStr[7]);
                current++;
            }
            current =0;
            System.out.println("\nEnter: p - predvous    n - next    # - session to edit    q - quit");
            String input = input().toLowerCase();
            if (input.equals("p")) {
                if(start-5>=0) start-=5;
            }else if (input.equals("n")) {
                if(start+5<goals.size()) start+=5;
            }else if (input.equals("q")) {
                return;
            }else{
                try {
                    int index = Integer.parseInt(input);
                    String[] arrOfStr = goals.get(index-1).split(", ", 10);
                    viewSessionTrainer(userName, arrOfStr);
                } catch (Exception e) {
                }
            }
        }
    }
    
    public static void viewMySessionsMember(String userName){
        int start=0, current=0;
        while(true){
            String statment = "SELECT session.session_id,session.max_size,session.date,session.start_time,session.end_time,session.room_num,session.cost,session_members.paid, session_members.session_members_id  "+
            "FROM session "+
            "inner join session_members on session.session_id= session_members.session_id where session_members.user_email='"+userName+"'";
            ArrayList<String> colums = new ArrayList<String>();
            colums.add("session_id");
            colums.add("date");
            colums.add("start_time");
            colums.add("end_time");
            colums.add("room_num");
            colums.add("cost");
            colums.add("paid");
            colums.add("session_members_id");
            ArrayList<String> goals = interactItem(statment,colums);
            

            clearScreen();
            System.out.println("MY SESSIONS\n");
            System.out.printf("%8s %15s %15s %15s %8s %8s %8s\n","Index","Date","Start Time","End Time","Room #","Cost","Paid");
            while(current<5&&start+current<goals.size()){
                String[] arrOfStr = goals.get(start+current).split(", ", 10);
                System.out.printf("%8d %15s %15s %15s %8s %8s %8s\n",start+current+1,arrOfStr[1],arrOfStr[2],arrOfStr[3],arrOfStr[4],arrOfStr[5],arrOfStr[6]);
                current++;
            }
            current =0;
            System.out.println("\nEnter: p - predvous    n - next    # - session to veiw   q - quit");
            String input = input().toLowerCase();
            if (input.equals("p")) {
                if(start-5>=0) start-=5;
            }else if (input.equals("n")) {
                if(start+5<goals.size()) start+=5;
            }else if (input.equals("q")) {
                return;
            }else{
                try {
                    clearScreen();
                    int index = Integer.parseInt(input);
                    String[] arrOfStr = goals.get(index-1).split(", ", 10);
                    System.out.printf("%8d %15s %15s %15s %8s %8s %8s\n",start+current+1,arrOfStr[1],arrOfStr[2],arrOfStr[3],arrOfStr[4],arrOfStr[5],arrOfStr[6]);
                    System.out.println("\nEnter: c - cancel    p - pay   q - quit");
                    input = input().toLowerCase();
                    if (input.equals("p")) {
                        patch("update session_members set paid= true where session_members_id="+arrOfStr[7]);

                    }else if (input.equals("c")) {
                        patch("delete from session_members where session_members_id="+arrOfStr[7]);
                        
                    }
                } catch (Exception e) {
                }
            }
        }
    }
    
    public static void searchMember(){
        int start=0, current=0;
        while(true){
            clearScreen();
            System.out.println("SEARCH MEMBERS\n");  
            System.out.print("\nFirst Name: ");  
            String first = input();
            System.out.print("\nlast Name: ");  
            String last = input();

            String statment = "select CONCAT(first_name, ' ', last_name) AS fullName,user_email,account_type,sex  from users where first_name like '"+first+"%' and last_name like '"+last+"%'";
            ArrayList<String> colums = new ArrayList<String>();
            colums.add("fullName");
            colums.add("user_email");
            colums.add("account_type");
            colums.add("sex");
            ArrayList<String> goals = interactItem(statment,colums);
            

            System.out.printf("%25s %20s %15s %5s\n","Full Name","Email","Account Type","Sex");
            while(current<5&&start+current<goals.size()){
                String[] arrOfStr = goals.get(start+current).split(", ", 10);
                System.out.printf("%25s %20s %15s %5s\n",arrOfStr[0],arrOfStr[1],arrOfStr[2],arrOfStr[3]);
                current++;
            }
            current =0;
            System.out.println("\nEnter: p - predvous    n - next    s - search   q - quit");
            String input = input().toLowerCase();
            if (input.equals("p")) {
                if(start-5>=0) start-=5;
            }else if (input.equals("n")) {
                if(start+5<goals.size()) start+=5;
            }else if (input.equals("q")) {
                return;
            }
        }
        
    }

    public static void viewequipment(String userName){
        int start=0, current=0;
        while(true){
            String statment = "SELECT * from equipment ORDER BY room_num ASC";
            ArrayList<String> colums = new ArrayList<String>();
            colums.add("equipment_id");
            colums.add("name");
            colums.add("open");
            colums.add("room_num");

            ArrayList<String> goals = interactItem(statment,colums);
            

            clearScreen();
            System.out.println("EQUIPMENT\n");
            System.out.printf("%8s %20s %8s %8s\n","Index","Name","Open","Room #");
            while(current<5&&start+current<goals.size()){
                String[] arrOfStr = goals.get(start+current).split(", ", 10);
                System.out.printf("%8s %20s %8s %8s\n",start+current+1,arrOfStr[1],arrOfStr[2],arrOfStr[3]);
                current++;
            }
            current =0;
            System.out.println("\nEnter: p - predvous    n - next    # - equipment to view    c - create    q - quit");
            String input = input().toLowerCase();
            if (input.equals("p")) {
                if(start-5>=0) start-=5;
            }else if (input.equals("n")) {
                if(start+5<goals.size()) start+=5;
            }else if (input.equals("q")) {
                return;
            }else if (input.equals("c")) {
                addEquipment();
            }else{
                try {
                    int index = Integer.parseInt(input);
                    String[] arrOfStr = goals.get(index-1).split(", ", 10);
                    viewequipmentMaintenance(userName,arrOfStr);
                } catch (Exception e) {
                }
            }
        }
    }
    
    public static void viewequipmentMaintenance(String userName,String[] arrOfStr){
        int start=0, current=0;
        while(true){
            String statment = "SELECT equipment_maintenance.comments,CONCAT(users.first_name, ' ', users.last_name) AS fullName "+
            "from equipment_maintenance "+
            "left join users on users.user_email=equipment_maintenance.user_email "+
            "where equipment_maintenance.equipment_id ="+arrOfStr[0];
            ArrayList<String> colums = new ArrayList<String>();
            colums.add("comments");
            colums.add("fullname");

            ArrayList<String> goals = interactItem(statment,colums);
            
            clearScreen();
            System.out.println("EQUIPMENT MAINENANCE\n");
            System.out.printf("%8s %20s %8s %8s\n","Index","Name","Open","Room #");
            System.out.printf("%8s %20s %8s %8s\n\n",start+current+1,arrOfStr[1],arrOfStr[2],arrOfStr[3]);
            System.out.printf("%30s %20s\n","Comments","Staff");
            while(current<5&&start+current<goals.size()){
                String[] arrOfStr2 = goals.get(start+current).split(", ", 10);
                System.out.printf("%30s %20s\n",arrOfStr2[0],arrOfStr2[1]);
                current++;
            }
            current =0;
            System.out.println("\nEnter: p - predvous    n - next    c - create maintence comment    e - update equipment    q - quit");
            String input = input().toLowerCase();
            if (input.equals("p")) {
                if(start-5>=0) start-=5;
            }else if (input.equals("n")) {
                if(start+5<goals.size()) start+=5;
            }else if (input.equals("q")) {
                return;
            }else if (input.equals("c")) {
                addEquipmentMaintenance(userName, arrOfStr[0]);
            }else if (input.equals("e")) {
                updateEquipment(arrOfStr[0]);
                return;
            }else if (input.equals("d")) {
                patch("delete from equipment_maintenance where equipment_id= "+arrOfStr[0]);
                patch("delete from equipment where equipment_id= "+arrOfStr[0]);
                return;
            }
        }
    }

//SQL Helpers
public static Boolean patch(String statment){
    Connection connection = connect();
    try {
        Statement statement = connection.createStatement();
        statement.executeQuery(statment);
        return true;
    } catch (Exception var6) {
        return false;
    }
}

public static ArrayList<String> interactItem(String statment, ArrayList<String> colunms){
    Connection connection = connect();
    try {
        ArrayList<String> items = new ArrayList<String>();
        Statement statement = connection.createStatement();
        statement.executeQuery(statment);
        ResultSet resultSet = statement.getResultSet();
        while(resultSet.next()){
            String row="";
            for (String c : colunms) {
                row = row + resultSet.getString(c)+", ";
            };
            //System.out.println(row);
            items.add(row);
        }
        //pause();
        return items;
    } catch (Exception var6) {
        return null;
    }
}


//UI Menu's
    public static void startupMenu() {
        Scanner input = new Scanner(System.in);
        while(true) {
            clearScreen();
            System.out.println("STARTUP MENU\n");
            System.out.println("1: Login");
            System.out.println("2: Create New User");
            System.out.println("3: Quit");
            String Responce = input.nextLine();
            if (Responce.equals("1")) {
                login();
            } else if (Responce.equals("2")) {
                addUser();
            } else {
                if (Responce.equals("3")) {
                    System.out.println("Goodbye");
                    break;
                }
                System.out.println("Need to enter a number between 1-3");
                pause();
            }
        }
        input.close();
    }

    public static void mainMenu(String userName, String Firstname, String LastName,String accountType) {
        while(true) {
            clearScreen();
            System.out.println("MAIN MENU - "+Firstname.toUpperCase()+" "+LastName.toUpperCase()+"\n");
            System.out.println("1: Profile");
            System.out.println("2: Dashboard");
            System.out.println("3: Schedule");
            if(accountType.equals("Trainer")) System.out.println("4: Trainer Menu");
            if(accountType.equals("Admin")) System.out.println("4: Admin Menu");
            if(accountType.equals("Member"))System.out.println("4: Log out");
            if(!accountType.equals("Member"))System.out.println("5: Log out");
            String Responce = input();
            if (Responce.equals("1")) {
                ProfileMenu(userName,Firstname,LastName);
            } else if (Responce.equals("2")) {
                DashboardMenu(userName,Firstname,LastName);
            } else if (Responce.equals("3")) {
                ScheduleMenu(userName,Firstname,LastName);
            }else if (Responce.equals("4")&&!accountType.equals("Member")) {
                if(accountType.equals("Trainer")) mainMenuTrainer(userName, Firstname,LastName,accountType);
                if(accountType.equals("Admin")) mainMenuAdmin(userName, Firstname,LastName,accountType);
            } else {
                if (Responce.equals("4")||Responce.equals("5")&&!accountType.equals("Member")) {
                    System.out.println("Goodbye");
                    break;
                }
                if(!accountType.equals("Member")) System.out.println("Need to enter a number between 1-5");
                else System.out.println("Need to enter a number between 1-4");
                pause();
            }
        }
    }

    public static void mainMenuTrainer(String userName, String Firstname, String LastName,String accountType){
        while(true) {
            clearScreen();
            System.out.println("TRAINER MAIN MENU - "+Firstname.toUpperCase()+" "+LastName.toUpperCase()+"\n");
            System.out.println("1: Sessions");
            System.out.println("2: Search Members");
            System.out.println("3: Back");
            String Responce = input();
            if (Responce.equals("1")) {
                viewMySessionsTrainer(userName);
            } else if (Responce.equals("2")) {
                searchMember();
            } else {
                return;
            }
        }
    }

    public static void mainMenuAdmin(String userName, String Firstname, String LastName,String accountType){
        while(true) {
            clearScreen();
            System.out.println("ADMIN MAIN MENU - "+Firstname.toUpperCase()+" "+LastName.toUpperCase()+"\n");
            System.out.println("1: Equipment");
            System.out.println("2: All Sessions");
            System.out.println("3: Back");
            String Responce = input();
            if (Responce.equals("1")) {
                viewequipment(userName);
            } else if (Responce.equals("2")) {
                viewSessionsAdmin(userName);
            } else {
                return;
            }
        }
    }

    public static void ProfileMenu(String userName, String Firstname, String LastName){
        while(true) {
            clearScreen();
            System.out.println("PROFILE MENU - "+Firstname.toUpperCase()+" "+LastName.toUpperCase()+"\n");
            System.out.println("1: Update Information");
            System.out.println("2: Update Fitness Goals");
            System.out.println("3: Add Fitness Goal");
            System.out.println("4: Add Health Metric");
            System.out.println("5: Add Exercise Routines");
            System.out.println("6: Back");
            String Responce = input();
            if (Responce.equals("1")) {
                updateInfo(userName, Firstname, LastName);
            } else if (Responce.equals("2")) {
                updateFitnessGoal(userName);
            } else if (Responce.equals("3")) {
                addFitnessGoal(userName);
            } else if (Responce.equals("4")) {
                addHealthMetic(userName);
            } else if (Responce.equals("5")) {
                addExerciseRoutine(userName);
            }else {
                if (Responce.equals("6")) {
                    break;
                }
                System.out.println("Need to enter a number between 1-6");
                pause();
            }
        }

    }
    
    public static void DashboardMenu(String userName, String Firstname, String LastName){
        while(true) {
            clearScreen();
            System.out.println("DASHBOARD MENU - "+Firstname.toUpperCase()+" "+LastName.toUpperCase()+"\n");
            System.out.println("1: Daily Exercise Routine");
            System.out.println("2: Fitness Achievements");
            System.out.println("3: Health Statistics");
            System.out.println("4: Back");
            String Responce = input();
            if (Responce.equals("1")) {
                viewExerciseRoutine(userName);
            } else if (Responce.equals("2")) {
                viewFitnessAchievements(userName);
            } else if (Responce.equals("3")) {
                viewHealthStatistics(userName);
            }else {
                if (Responce.equals("4")) {
                    break;
                }
                System.out.println("Need to enter a number between 1-4");
                pause();
            }
        }
    }
    
    public static void ScheduleMenu(String userName, String Firstname, String LastName){
        while(true) {
            clearScreen();
            System.out.println("SCHEDULE MENU - "+Firstname.toUpperCase()+" "+LastName.toUpperCase()+"\n");
            System.out.println("1: My Sessions");
            System.out.println("2: Open Sessions");
            System.out.println("3: Back");
            String Responce = input();
            if (Responce.equals("1")) {
                viewMySessionsMember(userName);
            } else if (Responce.equals("2")) {
                viewSessionsMember(userName);
            }else {
                if (Responce.equals("3")) {
                    break;
                }
                System.out.println("Need to enter a number between 1-3");
                pause();
            }
        }
    }


//helper functions
    public static void clearScreen() {  
        System.out.print("\033[H\033[2J");  
        System.out.flush();  
    }

    public static void pause() {  
        try{
            Thread.sleep(2000);
        }catch (InterruptedException e) {
            e.printStackTrace();
        }  
    }

    public static String input(){
        Scanner input = new Scanner(System.in);
        String result = input.nextLine();
        return result;
    }
}