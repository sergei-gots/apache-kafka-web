package org.appsdeveloperblog.estore.transfers.db;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransferRepository extends JpaRepository <@NotNull TransferEntity, @NotNull String> {
}
