package answer.king.controller;

import answer.king.Exception.BadRequestException;
import answer.king.model.Item;
import answer.king.service.ItemService;
import answer.king.validation.ItemValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/item")
public class ItemController {

    @Autowired
    private ItemService itemService;
    @Autowired
    private ItemValidator itemValidator;

    @RequestMapping(method = RequestMethod.GET)
    public List<Item> getAll() {
        return itemService.getAll();
    }

    @RequestMapping(method = RequestMethod.POST)
    public Item create(@Valid @RequestBody Item item) {
        if (!itemValidator.validateItem(item)) {
            throw new BadRequestException();
        }
        return itemService.save(item);
    }

    @RequestMapping(value = "/{id}/update", method = RequestMethod.PUT)
    public Item update(@PathVariable("id") Long id, @RequestBody BigDecimal price) {
        if (!itemValidator.validatePrice(price)) {
            throw new BadRequestException();
        }
        return itemService.update(id, price);
    }
}
