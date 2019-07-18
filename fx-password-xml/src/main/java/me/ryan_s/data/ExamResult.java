package me.ryan_s.data;

/**
 * Created by SimplyBallistic on 17/07/2019
 *
 * @author SimplyBallistic
 **/
public enum ExamResult {
    EXCELLENT('E'), PASS('P'), BRIBE('B');

    private char character;

    ExamResult(char character) {

        this.character = character;
    }

    public char getCharacter() {
        return character;
    }
}
