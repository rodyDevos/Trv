
package com.igames2go.t4f.data;

public class QuesWithAns {

    private QuesWithAnsDataObject number0 = new QuesWithAnsDataObject();
    private QuesWithAnsDataObject number1 = new QuesWithAnsDataObject();
    private QuesWithAnsDataObject number2 = new QuesWithAnsDataObject();
    private QuesWithAnsDataObject number3 = new QuesWithAnsDataObject();
    private QuesWithAnsDataObject number4 = new QuesWithAnsDataObject();

    public QuesWithAnsDataObject getQuestion() {
        return number0;
    }

    public QuesWithAnsDataObject getAnswer1() {
        return number1;
    }

    public QuesWithAnsDataObject getAnswer2() {
        return number2;
    }

    public QuesWithAnsDataObject getAnswer3() {
        return number3;
    }

    public QuesWithAnsDataObject getAnswer4() {
        return number4;
    }

    @Override
    public String toString() {

        return "QuestionWithAnswers: " + "\nnumber0: " + number0
                + "\nnumber1: " + number1 + "\nnumber2: " + number2
                + "\nnumber3: " + number3 + "\nnumber4: " + number4;

    }

}
