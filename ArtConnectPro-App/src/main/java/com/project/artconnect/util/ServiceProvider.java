package com.project.artconnect.util;

import com.project.artconnect.service.*;
import com.project.artconnect.service.impl.*;

/**
 * Service Provider to manage singleton instances of services and handle their
 * initialization.
 */
public class ServiceProvider {
    private static final InMemoryArtistService artistService = new InMemoryArtistService();
    private static final InMemoryArtworkService artworkService = new InMemoryArtworkService();
    private static final InMemoryGalleryService galleryService = new InMemoryGalleryService();
    private static final InMemoryWorkshopService workshopService = new InMemoryWorkshopService();
    private static final InMemoryCommunityService communityService = new InMemoryCommunityService();

    static {
        // Initialize services with their dependencies
        artworkService.initData(artistService);
        galleryService.initData(artworkService);
        workshopService.initData(artistService);
        communityService.initData(artworkService);
    }

    public static ArtistService getArtistService() {
        return new ArtistServiceImpl();
    }

    public static ArtworkService getArtworkService() {
        return new ArtworkServiceImpl();
    }

    public static GalleryService getGalleryService() {
        return new GalleryServiceImpl();
    }

    public static WorkshopService getWorkshopService() {
        return new WorkshopServiceImpl();
    }

    public static CommunityService getCommunityService() {
        return new CommunityServiceImpl();
    }

    public static ExhibitionService getExhibitionService() {
        return new ExhibitionServiceImpl();
    }
}
