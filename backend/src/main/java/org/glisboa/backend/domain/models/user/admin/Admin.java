package org.glisboa.backend.domain.models.user.admin;

import jakarta.persistence.DiscriminatorValue;
import org.glisboa.backend.domain.models.user.User;

@DiscriminatorValue("ADMIN")
public class Admin extends User {}