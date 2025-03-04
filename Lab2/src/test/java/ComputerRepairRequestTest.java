import model.ComputerRepairRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import repository.RequestRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class ComputerRepairRequestTest {
    @Test
    public void test1(){
        RequestRepository requestRepository = new RequestRepository();
        requestRepository.add(new ComputerRepairRequest());
        assertEquals(requestRepository.getAll().size(), 1);

    }
    @Test
    public void test2(){
        ComputerRepairRequest computerRepairRequest = new ComputerRepairRequest(1,"2","3","4","5","6","7");
        assertEquals(1,computerRepairRequest.getID());
    }
}
