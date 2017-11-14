package answer.king.service;

import answer.king.model.Item;
import answer.king.repo.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class ItemService {

    @Autowired
    private ItemRepository itemRepository;

    public List<Item> getAll() {
        return itemRepository.findAll();
    }

    public Item save(Item item) {
        return itemRepository.save(item);
    }

    public Item update(Long id, BigDecimal price) {
        Item existingItem = itemRepository.findOne(id);
        if (existingItem != null) {
            existingItem.setPrice(price);
            itemRepository.save(existingItem);
            return existingItem;
        }
        throw new EntityNotFoundException();
    }
}
