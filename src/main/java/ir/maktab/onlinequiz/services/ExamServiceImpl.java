package ir.maktab.onlinequiz.services;

import ir.maktab.onlinequiz.dao.ExamDAO;
import ir.maktab.onlinequiz.dao.QuestionDAO;
import ir.maktab.onlinequiz.dto.IdsListDTO;
import ir.maktab.onlinequiz.dto.QuestionDTO;
import ir.maktab.onlinequiz.models.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ExamServiceImpl implements ExamService {
    final ExamDAO examDAO;
    final QuestionDAO questionDAO;

    public ExamServiceImpl(ExamDAO examDAO, QuestionDAO questionDAO) {
        this.examDAO = examDAO;
        this.questionDAO = questionDAO;
    }

    @Override
    public void createQuestion(QuestionDTO questionDTO) {
        Optional<Exam> examOptional = examDAO.findById(questionDTO.getExamId());
        if (examOptional.isPresent()) {
            Exam exam = examOptional.get();
            MultipleChoiceQuestion choiceQuestion = new MultipleChoiceQuestion();
            DescriptiveQuestion descriptiveQuestion = new DescriptiveQuestion();
            if (questionDTO.getType().equals("multipleChoice")) {
                choiceQuestion.setText(questionDTO.getText());
                choiceQuestion.setTitle(questionDTO.getTitle());
                choiceQuestion.setAnswer(questionDTO.getAnswer());
                choiceQuestion.setTeacherCreator(exam.getCourse().getTeacher());
                choiceQuestion.setOptions(questionDTO.getOptions());

                if (choiceQuestion.getScoreList() == null || choiceQuestion.getScoreList().isEmpty())
                    choiceQuestion.setScoreList(List.of(new Score(null, questionDTO.getPoint(), choiceQuestion, exam)));
                else
                    choiceQuestion.getScoreList().add(new Score(null, questionDTO.getPoint(), choiceQuestion, exam));
                exam.getQuestions().add(choiceQuestion);
                examDAO.save(exam);

            } else if (questionDTO.getType().equals("descriptive")) {
                descriptiveQuestion.setText(questionDTO.getText());
                descriptiveQuestion.setTitle(questionDTO.getTitle());
                descriptiveQuestion.setAnswer(questionDTO.getAnswer());
                descriptiveQuestion.setTeacherCreator(exam.getCourse().getTeacher());

                if (descriptiveQuestion.getScoreList() == null || choiceQuestion.getScoreList().isEmpty())
                    descriptiveQuestion.setScoreList(List.of(new Score(null, questionDTO.getPoint(), descriptiveQuestion, exam)));
                else
                    descriptiveQuestion.getScoreList().add(new Score(null, questionDTO.getPoint(), descriptiveQuestion, exam));
                exam.getQuestions().add(descriptiveQuestion);
                examDAO.save(exam);
            }
        }
    }

    @Override
    public void addQuestionFromTeacherQuestionBankToExam(IdsListDTO idsListDTO) {
        Optional<Exam> examOptional = examDAO.findById(Long.parseLong(idsListDTO.getSecret()));
        if (examOptional.isPresent()) {
            Exam exam = examOptional.get();
            List<Question> questions = new ArrayList<>();
            questionDAO.findAllById(idsListDTO.getListId()
                    .stream()
                    .map(Long::parseLong)
                    .collect(Collectors.toList())).forEach(questions::add);
            if (exam.getQuestions() == null)
                exam.setQuestions(questions);
            else {
                exam.setQuestions(Stream.of(exam.getQuestions(), questions)
                        .flatMap(Collection::stream)
                        .collect(Collectors.toList()));
            }

            examDAO.save(exam);
        }
    }

    @Override
    public void deleteAllSelectedQuestionOfExam(IdsListDTO idsListDTO) {
        Optional<Exam> examOptional = examDAO.findById(Long.parseLong(idsListDTO.getSecret()));
        if (examOptional.isPresent()) {
            Exam exam = examOptional.get();
            if (exam.getQuestions() != null && !exam.getQuestions().isEmpty()) {
                List<Long> ids = idsListDTO.getListId()
                        .stream()
                        .map(Long::parseLong)
                        .collect(Collectors.toList());

                exam.getQuestions().removeIf(question -> ids.contains(question.getId()));
            }
            examDAO.save(exam);
        }
    }

    @Override
    public void deleteAllQuestionOfExam(Long examId) {
        Optional<Exam> examOptional = examDAO.findById(examId);
        if (examOptional.isPresent()) {
            Exam exam = examOptional.get();
            if (exam.getQuestions() != null && !exam.getQuestions().isEmpty()) {
                exam.getQuestions().removeAll(exam.getQuestions());
            }
            examDAO.save(exam);
        }
    }
}
