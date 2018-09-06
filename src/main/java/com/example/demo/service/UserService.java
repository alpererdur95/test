package com.example.demo.service;

import com.example.demo.exeptions.UserNotFoundException;
import com.example.demo.models.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    private static final Double limit = 5.0;
    public Iterable<User> getUsers() {
        return userRepository.findAll();
    }
    public User getUser(Integer uid) throws UserNotFoundException {
        return userRepository.findById(uid).orElseThrow(UserNotFoundException::new);
    }
    public User saveUser(User user) {

         return userRepository.save(user);
    }
    public void DeleteUser(Integer uid) {
        userRepository.deleteById(uid);
    }
    public void updatePaswoord(String pass,Integer id){
        userRepository.updatePassword(pass,id);
    }
    public List<User> findSearchUser(String search, int page, int limit) {
        Integer startAt = (page - 1) * limit;
        return userRepository.findSearchUser(search, startAt, limit);
    }
    public Integer getCount(String search) {
      Double sum =userRepository.getCount(search);
        Integer IntegerCount = 0;
        Integer count=0;
        Double value=0.0;
        Double currentValue;
        currentValue = sum/limit;
        IntegerCount =currentValue.intValue();
        value=IntegerCount.doubleValue();
if (value.equals(currentValue))
{ count =IntegerCount;
}
else if(sum==0){
    count=0;
}
else
{count=IntegerCount+1;}
return count;
    }
}