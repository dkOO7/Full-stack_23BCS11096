class Person {
    constructor(name, age) {
        this.name = name;
        this.age = age;
    }

    getDetails() {
        return `Name: ${this.name}, Age: ${this.age}`;
    }
}

class Student extends Person {
    constructor(name, age, grade) {
        super(name, age);
        this.grade = grade;
    }

    getDetails() {
        return `Student - ${super.getDetails()}, Grade: ${this.grade}`;
    }
}

class Teacher extends Person {
    constructor(name, age, subject) {
        super(name, age);
        this.subject = subject;
    }

    getDetails() {
        return `Teacher - ${super.getDetails()}, Subject: ${this.subject}`;
    }
}

const person1 = new Person("John Doe", 40);
const student1 = new Student("Alice", 18, "12th");
const teacher1 = new Teacher("Mr. Smith", 45, "Mathematics");

const people = [person1, student1, teacher1];

people.forEach(person => {
    console.log(person.getDetails());
});

console.log(student1 instanceof Person); 
console.log(student1 instanceof Student); 
console.log(teacher1 instanceof Teacher); 
console.log(person1 instanceof Student);
