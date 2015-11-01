import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Human {
    // Human instance Variables
    public String name;
    private int age;
    public int health;
    public String ethnicity;
    public String gender;

    public Human spouse;
    public LinkedList<Human> children;
    public LinkedList<Human> friends;

    public boolean employed;
    public int salary;

    // Constants and variables used within class
    private Random rand;
    private static final double AGE_PROBABILTY = 0.5;
    private static final double ETHNICITY_PROBABILITY = 0.2;
    private static final double BASE_FRIENDSHIP_PROBABILTY = 0.75;
    private static final ArrayList<String> randNames = getRandomPeople();

    /**
     * Initialize the instance variables for a human
     */
    public Human(String name, int age, String ethnicity, String gender, 
            int salary) {
        this.name = name;
        this.age = age;
        health = 1;
        this.ethnicity = ethnicity;
        this.gender = gender;

        spouse = null;
        children = new LinkedList<Human>();
        friends = new LinkedList<Human>();

        this.salary = salary;
        employed = salary > 0 ? true : false;

        rand = new Random();
        rand.setSeed(System.currentTimeMillis());
    }

    public Human(String name, int age, String ethnicity, String gender, 
                    int salary, long seed) {
        this(name, age, ethnicity, gender, salary);
        rand.setSeed(seed);
    }

    private static ArrayList<String> getRandomPeople() {
        ArrayList<String> lines = null;

        try {
            FileReader fileReader = new FileReader("names.txt");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            lines = new ArrayList<String>();
            String line = null;

            while ((line = bufferedReader.readLine()) != null) {
                lines.add(line);
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        } 

        return lines;
    }

    public int getAge() {
        return Simulator.currentYear - Simulator.startYear + age;
    }

    public String introduce() {
        return "Hi, my name is " + name + " and I'm " + age + " years old";
    }

    public String marry(Human toWed) {
        if (age < 18) {
            return name + " is too young to be married";
        } else if (toWed.name.equals(name)) {
            return name + "trying to marry them selves!! Unfortunatly " + 
                    name + " is still single";
        } else if (spouse == null && toWed.spouse == null) {
            spouse = toWed;
            toWed.spouse = this;

            return name + " and " + spouse.name + " are now married";
        } else {
            return (spouse != null ? name : toWed.name) + " is already married";
        }
    }

    public String divorce() {
        if (spouse != null) {
            String ret;

            ret =  name + " and " + spouse.name + " divorced";
            spouse.spouse = null;
            spouse = null;
            
            return ret;
        } else {
            return name + " isn't even married";
        }
    }

    // assumes giveBirth is called with a married Human
    public String giveBirth(Human child, ArrayList<Human> newChildren) {
        if (!gender.equals("female")) {
            return name + " has to be a female to give birth";
        }
        if (!spouse.gender.equals("male")) {
            return name + " can only give birth if her spouse is a male";
        }

        children.add(child);
        spouse.children.add(child);
        newChildren.add(child);

        return name + " and " + spouse.name + " gave birth to a baby named " + 
                child.name;
    }

    public String getJob(int money) {
        if (employed != true) {
            employed = true;
            salary = money;

            return name + " found a job that is paying " + 
                    (gender.equals("male") ? "him" : "her") + " " + 
                    salary + " per year";
        } else {
            return name + " already has a job";
        }
    }

    public String leaveJob() {
        if (employed == true) {
            employed = false;
            salary = 0;

            return name + " has left " + 
                    (gender.equals("male") ? "his" : "her") + " job";
        } else {
            return name + " does not have a job " + 
                    (gender.equals("male") ? "he" : "she") + " can leave";
        }
    }

    private int ageDiff(int otherAge) {
        return otherAge >= age ? otherAge - age : age - otherAge;
    }

    private String makeNewFriend(Human newFriend) {
        if (newFriend)
    }
    
    public String spendTimeWith(Human otherPerson) {
        if (friends.contains(otherPerson)) {
            return name + " had a great time hanging out with their friend " + otherPerson.name;
        } else {
            // possibly make a friend!!
            double chanceOfBeingFriends = FRIENDSHIO_BASE_PROBABILTY;
            // for every year you differ in age you decrease your chances of being friends by 10%
            chanceOfBeingFriends -= AGE_PROBABILTY * ageDiff(newFriend.age);
            // if you are a different ethnicity you decrease your changce of being friends by 30%
            int sameNationality = ethnicity.equals(newFriend.ethnicity) ? 0 : 1;
            chanceOfBeingFriends -= ETHNICITY_PROBABILITY * sameNationality;
            
            if (rand.nextDouble() <= chanceOfBeingFriends) {
                return makeNewFriend(newFriend);
            } else {
                return name + " did not become friends " + otherPerson.name + 
                                " after spending time together"; 
            }
        }
    }
    
    public String pursueMarrage() {
        if (friends.size() > 0)
            return marry(friends.get(rand.nextInt(friends.size())));
        else 
            return name + " does not have any friends to be able to marry :(";
    }
    
    private double fertilityProbability() {
        return -0.0016 * age * age + 0.0754 * age + 0.0352;
    }

    public String tryForKids(ArrayList<Human> listOfNewChildren) {
        if (spouse == null) {
            return name + " needs to be married to give birth";
        }
        String gender = rand.nextBoolean() ? "male" : "female";
        Human newChild = new Human(randNames.get(rand.nextInt(randNames.size())), 
                0, ethnicity, gender, 0);
        if (rand.nextDouble() < fertilityProbability())
            return gender.equals("male") && spouse != null ? 
                    spouse.giveBirth(newChild, listOfNewChildren) : 
                    giveBirth(newChild, listOfNewChildren);
        else 
            return "Wasn't able to have kids";
    }
    
    public String checkVitals(ArrayList<Human> deceased) {
        if (age < 30) {
            health++;
        } else {
            health--;
        }
        if (health < 0) {
            deceased.add(this);
            
            return "Unfortunatly " + name + "has passed away at age " + 
                    age;
        } else {
            return name + " is healthy as ever!"; 
                        //could change based on what his health value actually is
        }
    }
    
}