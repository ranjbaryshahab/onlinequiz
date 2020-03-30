package ir.maktab.onlinequiz.services;

import ir.maktab.onlinequiz.dao.ExamDAO;
import ir.maktab.onlinequiz.dao.MultipleChoiceQuestionDAO;
import ir.maktab.onlinequiz.dao.QuestionDAO;
import ir.maktab.onlinequiz.dao.StudentDAO;
import ir.maktab.onlinequiz.dto.*;
import ir.maktab.onlinequiz.models.*;
import ir.maktab.onlinequiz.utils.MyTime;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ExamServiceImpl implements ExamService {
    final ExamDAO examDAO;
    final QuestionDAO questionDAO;
    final StudentDAO studentDAO;
    final MultipleChoiceQuestionDAO multipleChoiceQuestionDAO;

    public ExamServiceImpl(ExamDAO examDAO, QuestionDAO questionDAO, StudentDAO studentDAO, MultipleChoiceQuestionDAO multipleChoiceQuestionDAO) {
        this.examDAO = examDAO;
        this.questionDAO = questionDAO;
        this.studentDAO = studentDAO;
        this.multipleChoiceQuestionDAO = multipleChoiceQuestionDAO;
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

    @Override
    public void editExam(CreateExamDTO createExamDTO) {
        Optional<Exam> examOptional = examDAO.findById(Long.parseLong(createExamDTO.getExamId()));
        if (examOptional.isPresent()) {
            Exam exam = examOptional.get();
            exam.setTime(MyTime.convertStringToTime(createExamDTO.getTime()));
            exam.setDescription(createExamDTO.getDescription());
            exam.setTitle(createExamDTO.getTitle());
            examDAO.save(exam);
        }
    }

    @Override
    public Long getExamTime(Long id) {
        Optional<Exam> examOptional = examDAO.findById(id);
        if (examOptional.isPresent()) {
            Exam exam = examOptional.get();
            long hour = exam.getTime().getHour();
            long minute = exam.getTime().getMinute();
            long second = exam.getTime().getSecond();
            return hour * 3600 * 1000 + minute * 60 * 1000 + second * 1000;
        }
        return 0L;
    }

    @Override
    public void addAnswerByStudent(Long examId, AnswersExamDTO answersExamDTO) {
        Optional<Exam> examOptional = examDAO.findById(examId);
        if (examOptional.isPresent()) {
            Exam exam = examOptional.get();
            Optional<Student> studentOptional = studentDAO.findByAccount_Username(answersExamDTO.getStudentUsername());
            if (studentOptional.isPresent()) {
                Student student = studentOptional.get();
                boolean isCorrect = false;
                Score score = null;
                for (AddAnswerDTO addAnswerDTO : answersExamDTO.getAddAnswersDTO()) {
                    if (exam.getStudentAnswers()
                            .stream()
                            .noneMatch(studentAnswer -> studentAnswer.getExam().getId().equals(examId) && studentAnswer.getStudent().getId().equals(student.getId()))) {
                        if (addAnswerDTO.getQuestionType().equals("multiple-choice")) {
                            Optional<MultipleChoiceQuestion> multipleChoiceQuestionOptional = multipleChoiceQuestionDAO.findById(addAnswerDTO.getQuestionId());
                            if (multipleChoiceQuestionOptional.isPresent()) {
                                MultipleChoiceQuestion multipleChoiceQuestion = multipleChoiceQuestionOptional.get();
                                if (multipleChoiceQuestion.getAnswer().equals(addAnswerDTO.getContext())) {
                                    isCorrect = true;
                                    score = multipleChoiceQuestion.getScoreList().stream().filter(score1 -> score1.getExam().getId().equals(exam.getId())).findAny().get();
                                }
                            }
                        }
                        boolean finalIsCorrect = isCorrect;
                        Score finalScore = score;
                        exam.getQuestions()
                                .stream()
                                .filter(question -> question.getId().equals(addAnswerDTO.getQuestionId()))
                                .findAny()
                                .ifPresent(question -> {
                                            if (question.getStudentAnswers().isEmpty()) {
                                                List<StudentAnswer> studentAnswers = new LinkedList<>();
                                                studentAnswers.add(new StudentAnswer(null, addAnswerDTO.getContext(), finalIsCorrect, student, exam, question, finalScore));
                                                question.setStudentAnswers(studentAnswers);
                                            } else
                                                question.getStudentAnswers().add(new StudentAnswer(null, addAnswerDTO.getContext(), finalIsCorrect, student, exam, question, finalScore));
                                        }
                                );
                        examDAO.save(exam);
                    }
                }
            }
        }

    }

    @Override
    public void endExam(Long id) {
        Optional<Exam> examOptional = examDAO.findById(id);
        examOptional.ifPresent(
                exam -> {
                    exam.setEnded(true);
                    examDAO.save(exam);
                }
        );
    }

    @Override
    public void startExam(Long id) {
        Optional<Exam> examOptional = examDAO.findById(id);
        examOptional.ifPresent(
                exam -> {
                    exam.setStarted(true);
                    examDAO.save(exam);
                }
        );
    }
}
