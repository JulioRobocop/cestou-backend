package com.pado.cestou.service;

import com.pado.cestou.model.*;
import com.pado.cestou.repository.ListingRepository;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ListingService {
    private final ListingRepository listingRepository;

    public ListingService(ListingRepository listingRepository) {this.listingRepository = listingRepository;}

    public Listing createListing(Listing listing) {
        listing.setStatus(Status.DISPONIVEL);
        listing.setBuyer(null);
        if (listing.getPrice() == null || listing.getPrice() <= 0) {
            throw new RuntimeException("O preço precisar ser diferente de null e maior que zero");
        }
        if (listing.getSeller() == null) {
            throw new RuntimeException("O anúncio precisa ter um vendedor");
        }
        return listingRepository.save(listing);
    }

    public Listing bookListing(Long listingId, Employee buyer) {
        Listing listing = listingRepository.findById(listingId).orElseThrow(() -> new RuntimeException("Anuncio não encontrado"));
        if (listing.getStatus() != Status.DISPONIVEL) {
            throw new RuntimeException("O anúncio já está reservado");
        }
        if (listing.getSeller().getId().equals(buyer.getId())) {
            throw new RuntimeException("O vendedor não pode reservar a própria cesta");
        }
        listing.setStatus(Status.RESERVADO);
        listing.setBuyer(buyer);
        return listingRepository.save(listing);
    }

    public Listing cancelBook(Long listingId, Employee requester) {
        Listing listing = listingRepository.findById(listingId).orElseThrow(() -> new RuntimeException("Anuncio não encontrado"));
        if (listing.getStatus() != Status.RESERVADO) {
            throw new RuntimeException("O anúncio não pode ser cancelado");
        }
        if (!listing.getBuyer().getId().equals(requester.getId()) && !listing.getSeller().getId().equals(requester.getId()) ) {
            throw new RuntimeException("Apenas o comprador ou vendedor pode cancelar a reserva");
        }
        listing.setStatus(Status.DISPONIVEL);
        listing.setBuyer(null);
        return listingRepository.save(listing);
    }

    public Listing cancelListing(Long listingId, Employee seller) {
        Listing listing = listingRepository.findById(listingId).orElseThrow(() -> new RuntimeException("Anuncio não encontrado"));
        if (listing.getStatus() != Status.DISPONIVEL && listing.getStatus() != Status.RESERVADO ) {
            throw new RuntimeException("Não é possível cancelar o anúncio");
        }
        if (!listing.getSeller().getId().equals(seller.getId())) {
            throw new RuntimeException("Apenas o vendedor pode cancelar o anúncio");
        }
        listing.setStatus(Status.CANCELADO);
        return listingRepository.save(listing);
    }

    public Listing concludedListing(Long listingId, Employee seller) {
        Listing listing = listingRepository.findById(listingId).orElseThrow(() -> new RuntimeException("Anúncio não encontrado"));
        if (listing.getStatus() != Status.RESERVADO) {
            throw new RuntimeException("Não é possível concluir o anúncio");
        }
        if (!listing.getSeller().getId().equals(seller.getId())) {
            throw new RuntimeException("Apenas o vendedor pode concluir o anúncio");
        }
        listing.setStatus(Status.CONCLUIDO);
        return listingRepository.save(listing);
    }

    public List<Listing> availableListing(Sector sector, WorkShift workShift) {
        return listingRepository.findAvailableWithFilters(Status.DISPONIVEL, sector, workShift);
    }

    public List<Listing> sellerListing(Employee seller) {
        return listingRepository.findBySeller(seller);
    }

    public List<Listing> buyerListing(Employee buyer) {
        return listingRepository.findByBuyer(buyer);
    }

}


