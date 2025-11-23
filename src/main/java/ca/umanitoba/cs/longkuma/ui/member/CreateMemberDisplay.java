package ca.umanitoba.cs.longkuma.ui.member;

import ca.umanitoba.cs.longkuma.logic.member.Member;
import com.google.common.base.Preconditions;

import java.util.Scanner;

import static ca.umanitoba.cs.longkuma.ui.OutputTools.red;

public class CreateMemberDisplay {
    private final Scanner keyboard;

    public CreateMemberDisplay() {this.keyboard = new Scanner(System.in);}

    public Member createMember() {
        Member.MemberBuilder builder = new Member.MemberBuilder();
        System.out.println("Let's create a new member: ");
        getNameInput(builder);
        return builder.build();
    }

    private void getNameInput(Member.MemberBuilder builder) {
        String name;
        Preconditions.checkNotNull(builder, "Builder should not be null");

        do {
            System.out.println("Enter a name: ");

            name = keyboard.nextLine();
            try {
                builder.name(name);
            } catch (Exception e) { // TODO: Change to specific exception
                red("Names must have at least one letter, e.g., Ada");
                name = null;
            }
        } while (name == null);

        Preconditions.checkNotNull(name, "Name should not be null after it's been set.");
    }
}