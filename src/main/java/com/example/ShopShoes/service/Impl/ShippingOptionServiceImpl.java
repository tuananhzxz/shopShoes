package com.example.ShopShoes.service.Impl;

import com.example.ShopShoes.dto.ShippingOptionDTO;
import com.example.ShopShoes.entity.ShippingOption;
import com.example.ShopShoes.repository.ShippingOptionRepository;
import com.example.ShopShoes.service.ShippingOptionService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ShippingOptionServiceImpl implements ShippingOptionService {

        private ShippingOptionRepository shippingOptionRepository;
        private ModelMapper modelMapper;

        @Override
        public List<ShippingOptionDTO> getAllShippingOptions() {
            List<ShippingOption> shippingOptions = shippingOptionRepository.findAll();
            return shippingOptions.stream().map(shippingOption -> modelMapper.map(shippingOption, ShippingOptionDTO.class)).toList();
        }
}
