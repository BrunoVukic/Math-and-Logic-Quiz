package com.example.MathAndLogicQuiz;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class LevelPick
{
    int correctAnswer;
    int readLevel;
    String hint;
    public void chooseLevel() {

        switch (readLevel) {
            case 1:
                correctAnswer = 7;
                hint = "Number after 6";
                break;
            case 2:
                correctAnswer = 15;
                hint = "Number before 16";
                break;
            case 3:
                correctAnswer = 42;
                hint = "Number multiplied by 7";
                break;
            case 4:
                correctAnswer = 8;
                hint = "Multiply with 8";
                break;
            case 5:
                correctAnswer = 17;
                hint = "Adding with a number between 10 and 14";
                break;
            case 6:
                correctAnswer = 20;
                hint = "Adding with a number between 10 and 15";
                break;
            case 7:
                correctAnswer = 24;
                hint = "Multiply then add one more";
                break;
            case 8:
                correctAnswer = 20;
                hint = "Add a multiplication of 2 ";
                break;
            case 9:
                correctAnswer = 13;
                hint = "Add a number, subtract it, add again";
                break;
            case 10:
                correctAnswer = 4;
                hint = "Multiply!";
                break;
            case 11:
                correctAnswer = 10;
                hint = "Multiply first, add second";
                break;
            case 12:
                correctAnswer = 49;
                hint = "Multiply first, add and subtract second";
                break;
            case 13:
                correctAnswer = 29;
                hint = "1.Brackets, 2.Multiplications, 3.Adding and subtracting";
                break;
            case 14:
                correctAnswer = 2;
                hint = "Multiply first, add and subtract second";
                break;
            case 15:
                correctAnswer = 10;
                hint = "Multiply digit with a digit";
                break;
            case 16:
                correctAnswer = 14;
                hint = "Count the big ones";
                break;
            case 17:
                correctAnswer = 87;
                hint = "Flip the phone upside down";
                break;
            case 18:
                correctAnswer = 4;
                hint = "x-y=60\n+x+y=100";
                break;
            case 19:
                correctAnswer = 15;
                hint = "Divide and multiply, then add";
                break;
            case 20:
                correctAnswer = 12;
                hint = "From left to right";
                break;
            case 21:
                correctAnswer = 30;
                hint = "Number below is the multiplier";
                break;
            case 22:
                correctAnswer = 7;
                hint = "Add the top to middle to get bottom";
                break;
            case 23:
                correctAnswer = 3;
                hint = "Add the columns";
                break;
            case 24:
                String date=new SimpleDateFormat("ddMM", Locale.getDefault()).format(new Date());
                hint = "Enter today's date(DD/MM)";
                break;
            case 25:
                correctAnswer = 18;
                hint = "Add digits into numbers, then add those numbers ";
                break;
            case 26:
                correctAnswer = 5;
                hint = "Multiply with 3 then subtract";
                break;
            case 27:
                correctAnswer = 48;
                hint = "Multiples of 6";
                break;
            case 28:
                correctAnswer = 2;
                hint = "Add all the numbers in a row";
                break;
            case 29:
                correctAnswer = 2;
                hint = "Zig-zag pattern";
                break;
            case 30:
                correctAnswer = 19;
                hint = "4^2=16\n1=4/4";
                break;
            case 31:
                correctAnswer = 2;
                hint = "Even numbers";
                break;
            case 32:
                correctAnswer = 8;
                hint = "Incrementing the number you add by 1";
                break;
            case 33:
                correctAnswer = 15;
                hint = "Pattern of added numbers";
                break;
            case 34:
                correctAnswer = 21;
                hint = "Add power of 2 (2^n)";
                break;
            case 35:
                correctAnswer = 29;
                hint = "Adding number to the right side to get the opposite side number";
                break;
            case 36:
                correctAnswer = 1;
                hint = "Add numbers on the same side of the triangle";
                break;
            case 37:
                correctAnswer = 46;
                hint = "Add odd numbers";
                break;
            case 38:
                correctAnswer = 10;
                hint = "Multipy base numbers, add to top";
                break;
            case 39:
                correctAnswer = 0;
                hint = "Count the circles in numbers";
                break;
            case 40:
                correctAnswer = 7;
                hint = "Top and bottom half are equal";
                break;
            case 41:
                correctAnswer = 8;
                hint = "Fibonacci";
                break;
            case 42:
                correctAnswer = 8;
                hint = "Top square +3,+2";
                break;
            case 43:
                correctAnswer = 56;
                hint = "Do the opposite operations";
                break;
            case 44:
                correctAnswer = 49;
                hint = "Number multiplied by itself";
                break;
            case 45:
                correctAnswer = 42;
                hint = "Multiply left number with (left number + 1)";
                break;
            case 46:
                correctAnswer = 4370;
                hint = "Patterns";
                break;
            case 47:
                correctAnswer = 14;
                hint = "| = 10 , V = 5 , X = 10";
                break;
            case 48:
                correctAnswer = 23;
                hint = "Prime numbers";
                break;
            case 49:
                correctAnswer = 15;
                hint = "Subtract opposite numbers to get the middle";
                break;
            case 50:
                correctAnswer = 16;
                hint = "Add a number to left triangle numbers";
                break;
            case 51:
                correctAnswer = 42;
                hint = "Angles in a triangle add up to 180";
                break;
            case 52:
                correctAnswer = 55;
                hint = "A^2 + B";
                break;
            case 53:
                correctAnswer = 18;
                hint = "Add a number, but in a zig-zag pattern";
                break;
            case 54:
                correctAnswer = 3;
                hint = "Multiply with middle to get the opposite";
                break;
            case 55:
                correctAnswer = 54;
                hint = "Multiply with 2 then subtract";
                break;
            case 56:
                correctAnswer = 29;
                hint = "AB=B (A+B)";
                break;
            case 57:
                correctAnswer = 13;
                hint = "\u221AA + \u221AB";
                break;
            case 58:
                correctAnswer = 9;
                hint = "Add and subtract opposite numbers to get inner ones";
                break;
            case 59:
                correctAnswer = 3;
                hint = "Add 2 diagonal numbers";
                break;
            case 60:
                correctAnswer = 12;
                hint = "Add outer numbers, add 2, get middle";
                break;
            case 61:
                correctAnswer = 3;
                hint = "(A + B)/(A - B)";
                break;
            case 62:
                correctAnswer = 81;
                hint = "3^2 , (3+3)^2 ,...";
                break;
            case 63:
                correctAnswer = 10;
                hint = "Middle of the two numbers below";
                break;
            case 64:
                correctAnswer = 3;
                hint = "Add the digits";
                break;
            case 65:
                correctAnswer = 93;
                hint = "-100, -150 , -200 , -250 ";
                break;
            case 66:
                correctAnswer = 51;
                hint = "Multiply with 2 then subtract";
                break;
            case 67:
                correctAnswer = 7;
                hint = "Add neighbouring numbers and subtract with a number";
                break;
            case 68:
                correctAnswer = 6;
                hint = "Add 2 lower numbers to get the third";
                break;
            case 69:
                correctAnswer = 420;
                hint = "Marijuana number";
                break;
            case 70:
                correctAnswer = 44;
                hint = "Increment circle segment to get the neighbouring circle segment";
                break;
            case 71:
                correctAnswer = 27;
                hint = "Cubed number";
                break;
            case 72:
                correctAnswer = 9;
                hint = "Do something with the top numbers to get the bottom ones";
                break;
            case 73:
                correctAnswer = 6;
                hint = "Add the numbers in the little triangles";
                break;
            case 74:
                correctAnswer = 8;
                hint = "2x2=4\n√4=2";
                break;
            case 75:
                correctAnswer = 30;
                hint = "Add a number to the numbers in the left diamond to get the numbers on the right";
                break;
            case 76:
                correctAnswer = 6;
                hint = "Add numbers on the same side";
                break;
            case 77:
                correctAnswer = 3;
                hint = "Add left and right, divide with a number to get the middle";
                break;
            case 78:
                correctAnswer = 22;
                hint = "(AxB) - (A-B)";
                break;
            case 79:
                correctAnswer = 7;
                hint = "Add outer numbers and divide with a number to get the middle";
                break;
            case 80:
                correctAnswer = 1;
                hint = "Count the number 13";
                break;
            default:correctAnswer = 7;
                hint = "Something went wrong";
                break;
        }
    }
}
