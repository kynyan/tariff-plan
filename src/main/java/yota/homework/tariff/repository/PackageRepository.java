package yota.homework.tariff.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yota.homework.tariff.model.Package;

public interface PackageRepository extends JpaRepository<Package, Long> {
}
