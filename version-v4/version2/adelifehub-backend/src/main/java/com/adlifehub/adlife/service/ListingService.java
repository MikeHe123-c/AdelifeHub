package com.adlifehub.adlife.service;
import com.adlifehub.adlife.mapper.FavoriteMapper; import com.adlifehub.adlife.mapper.ListingMapper; import com.adlifehub.adlife.model.Listing;
import org.springframework.stereotype.Service; import java.time.OffsetDateTime; import java.util.List;
@Service public class ListingService {
  private final ListingMapper listingMapper; private final FavoriteMapper favoriteMapper;
  public ListingService(ListingMapper listingMapper, FavoriteMapper favoriteMapper){ this.listingMapper=listingMapper; this.favoriteMapper=favoriteMapper; }
  public List<Listing> list(String type,int offset,int limit){ return listingMapper.list(type,offset,limit); }
  public int count(String type){ return listingMapper.count(type); }
  public List<Listing> listByAuthor(Long authorId,String status,int offset,int limit){ return listingMapper.listByAuthor(authorId,status,offset,limit); }
  public int countByAuthor(Long authorId,String status){ return listingMapper.countByAuthor(authorId,status); }
  public Listing getVisible(Long id){ Listing l=listingMapper.findById(id); if(l==null) return null; if(!"active".equals(l.getStatus()) && !"closed".equals(l.getStatus())) return null; return l; }
  public Listing create(Listing l, Long authorId){ l.setAuthorId(authorId); l.setStatus("active"); l.setCreatedAt(OffsetDateTime.now()); l.setUpdatedAt(OffsetDateTime.now()); listingMapper.insert(l); return listingMapper.findById(l.getId()); }
  public Listing update(Long id, Listing patch, Long actorId, boolean isAdmin){ Listing db=listingMapper.findById(id); if(db==null) return null; if(!isAdmin && !db.getAuthorId().equals(actorId)) throw new SecurityException("forbidden"); if(patch.getTitle()!=null) db.setTitle(patch.getTitle()); if(patch.getContent()!=null) db.setContent(patch.getContent()); if(patch.getPrice()!=null) db.setPrice(patch.getPrice()); if(patch.getPriceUnit()!=null) db.setPriceUnit(patch.getPriceUnit()); if(patch.getLocation()!=null) db.setLocation(patch.getLocation()); if(patch.getLatitude()!=null) db.setLatitude(patch.getLatitude()); if(patch.getLongitude()!=null) db.setLongitude(patch.getLongitude()); if(patch.getImages()!=null) db.setImages(patch.getImages()); db.setUpdatedAt(OffsetDateTime.now()); listingMapper.update(db); return listingMapper.findById(id); }
  public void softDelete(Long id, Long actorId, String reason, boolean isAdmin){ Listing db=listingMapper.findById(id); if(db==null) return; if(!isAdmin && !db.getAuthorId().equals(actorId)) throw new SecurityException("forbidden"); listingMapper.softDelete(id, OffsetDateTime.now(), actorId, reason); favoriteMapper.deleteFavoritesByListing(id); }
  public void restore(Long id){
    listingMapper.restore(id); }
}
