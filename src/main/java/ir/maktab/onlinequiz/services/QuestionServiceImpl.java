package ir.maktab.onlinequiz.services;

import ir.maktab.onlinequiz.dao.DescriptiveQuestionDAO;
import ir.maktab.onlinequiz.dao.MultipleChoiceQuestionDAO;
import ir.maktab.onlinequiz.dao.QuestionDAO;
import ir.maktab.onlinequiz.dto.QuestionDTO;
import ir.maktab.onlinequiz.models.DescriptiveQuestion;
import ir.maktab.onlinequiz.models.MultipleChoiceQuestion;
import ir.maktab.onlinequiz.models.Score;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class QuestionServiceImpl implements QuestionService {
    final MultipleChoiceQuestionDAO multipleChoiceQuestionDAO;
    final DescriptiveQuestionDAO descriptiveQuestionDAO;
    final QuestionDAO questionDAO;

    public QuestionServiceImpl(MultipleChoiceQuestionDAO multipleChoiceQuestionDAO, DescriptiveQuestionDAO descriptiveQuestionDAO, QuestionDAO questionDAO) {
        this.multipleChoiceQuestionDAO = multipleChoiceQuestionDAO;
        this.descriptiveQuestionDAO = descriptiveQuestionDAO;
        this.questionDAO = questionDAO;
    }

    @Override
    public Page<DescriptiveQuestion> descriptiveQuestionPaginate(Long examId, Pageable pageable) {
        return descriptiveQuestionDAO.findAllByExam_Id(examId, pageable);
    }


    @Override
    public Page<MultipleChoiceQuestion> multipleChoiceQuestionPaginate(Long examId, Pageable pageable) {
        return multipleChoiceQuestionDAO.findAllByExam_Id(examId, pageable);
    }

    @Override
    public Page<MultipleChoiceQuestion> multipleQuestionPaginateTeacherQuestionBank(String username, Pageable pageable) {
        return multipleChoiceQuestionDAO.findAllByTeacherCreator_Account_Username(username, pageable);
    }

    @Override
    public Page<DescriptiveQuestion> descriptiveQuestionPaginateTeacherQuestionBank(String username, Pageable pageable) {
        return descriptiveQuestionDAO.findAllByTeacherCreator_Account_Username(username, pageable);
    }


    @Override
    public void editQuestion(QuestionDTO questionDTO) {
        if (questionDTO.getType().equals("multipleChoice")) {
            Optional<MultipleChoiceQuestion> multipleChoiceQuestionOptional = multipleChoiceQuestionDAO.findById(questionDTO.getQuestionId());
            if (multipleChoiceQuestionOptional.isPresent()) {
                MultipleChoiceQuestion multipleChoiceQuestion = multipleChoiceQuestionOptional.get();
                multipleChoiceQuestion.setOptions(questionDTO.getOptions());
                multipleChoiceQuestion.setAnswer(questionDTO.getAnswer());
                multipleChoiceQuestion.setText(questionDTO.getText());
                multipleChoiceQuestion.setTitle(questionDTO.getTitle());
                Optional<Score> score = multipleChoiceQuestion.getScoreList()
                        .stream()
                        .filter(score1 -> score1.getQuestion().equals(multipleChoiceQuestion))
                        .findAny();

                score.ifPresent(score1 -> {
                    score1.setPoint(questionDTO.getPoint());
                });

                List<Score> scoreList = new ArrayList<>();
                scoreList.add(new Score(
                        null,
                        questionDTO.getPoint(),
                        multipleChoiceQuestion,
                        multipleChoiceQuestion.getExam().stream()
                                .filter(exam -> exam.getId().equals(questionDTO.getExamId())).findAny().get()));
                if (score.isEmpty()) {
                    multipleChoiceQuestion.setScoreList(scoreList);
                }
                multipleChoiceQuestionDAO.save(multipleChoiceQuestion);
            }
        } else if (questionDTO.getType().equals("descriptive")) {
            Optional<DescriptiveQuestion> descriptiveQuestionOptional = descriptiveQuestionDAO.findById(questionDTO.getQuestionId());
            if (descriptiveQuestionOptional.isPresent()) {
                DescriptiveQuestion descriptiveQuestion = descriptiveQuestionOptional.get();
                descriptiveQuestion.setAnswer(questionDTO.getAnswer());
                descriptiveQuestion.setText(questionDTO.getText());
                descriptiveQuestion.setTitle(questionDTO.getTitle());
                Optional<Score> score = descriptiveQuestion.getScoreList()
                        .stream()
                        .filter(score1 -> score1.getQuestion().equals(descriptiveQuestion))
                        .findAny();

                score.ifPresent(score1 -> {
                    score1.setPoint(questionDTO.getPoint());
                });

                List<Score> scoreList = new ArrayList<>();
                scoreList.add(new Score(
                        null,
                        questionDTO.getPoint(),
                        descriptiveQuestion,
                        descriptiveQuestion.getExam().stream()
                                .filter(exam -> exam.getId().equals(questionDTO.getExamId())).findAny().get()));
                if (score.isEmpty()) {
                    descriptiveQuestion.setScoreList(scoreList);
                }
                descriptiveQuestionDAO.save(descriptiveQuestion);
            }
        }
    }

    @Override
    public List<DescriptiveQuestion> descriptiveQuestionPaginateStudent(Long examId) {
        return descriptiveQuestionDAO.findAllByExam_Id(examId);
    }

    @Override
    public List<MultipleChoiceQuestion> multipleChoiceQuestionPaginateStudent(Long examId) {
        return multipleChoiceQuestionDAO.findAllByExam_Id(examId);
    }
}
