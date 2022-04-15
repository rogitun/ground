package heading.ground.service;

import heading.ground.entity.user.BaseUser;
import heading.ground.entity.util.Message;
import heading.ground.forms.util.MsgForm;
import heading.ground.repository.user.UserRepository;
import heading.ground.repository.util.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UtilService {

    private final UserRepository userRepository;
    private final MessageRepository messageRepository;

    @Transactional
    public void makeMsg(MsgForm form) {
        BaseUser receiver = userRepository.findById(form.getReceiverId()).get();
        BaseUser sender = userRepository.findById(form.getSenderId()).get();
        Message message = new Message(sender, receiver, form);
        messageRepository.save(message);
    }


    public List<Message> getMessages(Long id) {
        return messageRepository.findAllMessagesById(id);
    }

    @Transactional
    public void msgRead(Message msg) {
        msg.read();
    }

    public MsgForm makeReply(Message msg) {
        MsgForm msgForm = new MsgForm();
        msgForm.setReply(msg);
        return msgForm;
    }
}
